/**
 * 나무박멸
 * 삼성 SW 역량테스트 2022 상반기 오후 2번 문제
 * https://www.codetree.ai/training-field/frequent-problems/problems/tree-kill-all/description?page=1&pageSize=20
 * 
 * @author minchae
 * @date 2023. 10. 21.
 * 
 * 두 번째 풀이 방법
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer;

public class Main2 {
	
	// 상하좌우, 대각선
	static int[] dx = {-1, 1, 0, 0, -1, -1, 1, 1};
	static int[] dy = {0, 0, -1, 1, -1, 1, -1, 1};
	
	static int n, m, k, c;
	
	static int[][] map; // 나무와 벽의 위치 저장 (빈 칸은 0, 벽은 -1)
	static int[][] die; // 제초제를 뿌린 위치 저장
	
	static int result;

	public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        k = Integer.parseInt(st.nextToken());
        c = Integer.parseInt(st.nextToken());
        
        map = new int[n][n];
        die = new int[n][n];
        
        for (int i = 0; i < n; i++) {
        	st = new StringTokenizer(br.readLine());
        	
        	for (int j = 0; j < n; j++) {
        		map[i][j] = Integer.parseInt(st.nextToken());
        	}
        }
        
        while (m-- > 0) {
        	grow();
        	spread();
        	remove();
        	pick();
        }

        System.out.println(result);
	}
	
	// 인접한 네 개의 칸 중 나무가 있는 칸의 수만큼 나무가 성장
	private static void grow() {
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				// 나무가 없는 곳인 경우 다음으로 넘어감
				if (map[i][j] <= 0) {
					continue;
				}
				
				int cnt = 0;
				
				for (int d = 0; d < 4; d++) {
					int nx = i + dx[d];
					int ny = j + dy[d];
					
					if (checkRange(nx, ny) && map[nx][ny] > 0) {
						cnt++;
					}
				}
				
				map[i][j] += cnt;
			}
		}
	}
	
	// 번식 진행
	private static void spread() {
		int[][] newMap = new int[n][n];
		
		for (int i = 0; i < n; i++) {
			newMap[i] = Arrays.copyOf(map[i], n);
		}
		
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (map[i][j] <= 0) {
					continue;
				}
				
				int cnt = 0;
				
				ArrayList<int[]> list = new ArrayList<>();
				
				for (int d = 0; d < 4; d++) {
					int nx = i + dx[d];
					int ny = j + dy[d];
					
					if (checkRange(nx, ny) && map[nx][ny] == 0 && die[nx][ny] == 0) {
						cnt++;
						list.add(new int[] {nx, ny});
					}
				}
				
				// 각 칸에 번식 진행
				for (int[] tree : list) {
					newMap[tree[0]][tree[1]] += map[i][j] / cnt;
				}
			}
		}
		
		map = newMap;
	}
	
	// 제초제 삭제
	private static void remove() {
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (die[i][j] > 0) {
					die[i][j]--;
				}
			}
		}
	}
	
	// 제초제 뿌릴 위치 선정
	private static void pick() {
		int maxCnt = 0;
		int maxX = 0;
		int maxY = 0;
		
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				// 나무가 없는 곳인 경우 다음으로 넘어감
				if (map[i][j] <= 0) {
					continue;
				}
				
				int cnt = map[i][j];
				
				// 대각선 방향으로 퍼뜨림
				for (int d = 4; d < 8; d++) {
					int nx = i;
					int ny = j;
					
					for (int s = 0; s < k; s++) {
						nx += dx[d];
						ny += dy[d];
						
						// 범위를 벗어나거나 나무가 없는 곳인 경우에는 개수를 더이상 셀 필요가 없기 때문에 break
						if (!checkRange(nx, ny) || map[nx][ny] <= 0) {
							break;
						}
						
						cnt += map[nx][ny];
					}
				}
				
				// 최댓값 갱신
				if (cnt > maxCnt) {
					maxCnt = cnt;
					maxX = i;
					maxY = j;
				}
			}
		}
		
		sprinkle(maxX, maxY, maxCnt);
	}
	
	// 제초제 뿌리기
	private static void sprinkle(int x, int y, int cnt) {
		result += cnt;
		
		map[x][y] = 0;
		die[x][y] = c;
		
		// 대각선 방향으로 퍼뜨림
		for (int d = 4; d < 8; d++) {
			int nx = x;
			int ny = y;
			
			// k칸까지 퍼뜨림
			for (int s = 0; s < k; s++) {
				nx += dx[d];
				ny += dy[d];
				
				// 범위를 벗어나거나 벽인 경우 break (벽은 제초제의 영향을 받지 않기 때문에 바로 break 해줌)
				if (!checkRange(nx, ny) || map[nx][ny] == -1) {
					break;
				}
				
				// 빈칸인 경우에는 제초제 뿌림 (빈칸을 만난 경우에만 해당 칸까지만 제초제를 뿌리기 때문에 break)
				if (map[nx][ny] == 0) {
					die[nx][ny] = c;
					break;
				}
				
				map[nx][ny] = 0;
				die[nx][ny] = c;
			}
		}
	}
	
	private static boolean checkRange(int x, int y) {
		return x >= 0 && x < n && y >= 0 && y < n;
	}

}
