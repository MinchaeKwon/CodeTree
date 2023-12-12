/**
 * 나무박멸
 * 
 * @author minchae
 * @date 2023. 12. 12.
 */

import java.io.*;
import java.util.*;

public class TreeKillAll {
	
	// 상하좌우, 대각선
	static int[] dx = {-1, 1, 0, 0, -1, -1, 1, 1};
	static int[] dy = {0, 0, -1, 1, 1, -1, 1, -1};
	
	static int n, m, k, c;
	static int[][] map; // 총 나무의 그루 수는 1 이상 100 이하의 수, 빈 칸은 0, 벽은 -1
	
	static int[][] die;
	
	static int answer;

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
			breeding();
			
			remove(); // 여기서 제초제가 남은 년수를 감소시켜야 올바른 답이 나옴 (번식 시키기 전에 제초제가 0이 되는 경우에 꼬이게 됨)
			
			int[] pos = pick();
			sprinkle(pos);
		}
		
		System.out.println(answer);
	}
	
	// 1. 인접한 네 개의 칸 중 나무가 있는 칸의 수만큼 나무가 성장
	private static void grow() {
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (map[i][j] <= 0) {
					continue;
				}
				
				int cnt = 0;
				
				for (int d = 0; d < 4; d++) {
					int nx = i + dx[d];
					int ny = j + dy[d];
					
					if (isRange(nx, ny) && map[nx][ny] > 0) {
						cnt++;
					}
				}
				
				map[i][j] += cnt;
			}
		}
	}
	
	// 2. 기존에 있었던 나무들은 인접한 4개의 칸 중 벽, 다른 나무, 제초제 모두 없는 칸에 번식을 진행
	private static void breeding() {
		int[][] newMap = new int[n][n];
		
		for (int i = 0; i <n; i++) {
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
					
					if (isRange(nx, ny) && map[nx][ny] == 0 && die[nx][ny] == 0) {
						cnt++;
						list.add(new int[] {nx, ny});
					}
				}
				
				for (int[] blank : list) {
					newMap[blank[0]][blank[1]] += map[i][j] / cnt;	
				}
			}
		}
		
		map = newMap;
	}
	
	// 3. 제초제를 뿌릴 위치 선정 (각 칸 중 제초제를 뿌렸을 때 나무가 가장 많이 박멸되는 칸)
	private static int[] pick() {
		int maxX = 0;
		int maxY = 0;
		int maxCnt = 0;
		
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (map[i][j] <= 0) {
					continue;
				}
				
				int cnt = map[i][j];
				
				for (int d = 4; d < 8; d++) {
					int nx = i;
					int ny = j;
					
					// 해당 대각선 방향으로 k칸만큼 전파
					for (int s = 0; s < k; s++) {
						nx += dx[d];
						ny += dy[d];
						
						// 범위를 벗어나거나 벽 또는 빈칸일 경우 제초제에 박멸되는 나무가 없으므로 개수를 세지 않음
						// 해당 for문 벗어나서 다음 대각선 방향 탐색해야 하기 때문에 break
						if (!isRange(nx, ny) || map[nx][ny] <= 0) {
							break;
						}
						
						cnt += map[nx][ny];
					}
				}
				
				// 최댓값 갱신
				if (cnt > maxCnt) {
					maxX = i;
					maxY = j;
					maxCnt = cnt;
				}
			}
		}
		
		answer += maxCnt; // 박멸한 나무 수 증가
		
		return new int[] {maxX, maxY};
	}
	
	// 4. 제초제를 뿌림
	private static void sprinkle(int[] pos) {
		int x = pos[0];
		int y = pos[1];
		
		map[x][y] = 0;
		die[x][y] = c;
		
		for (int d = 4; d < 8; d++) {
			int nx = x;
			int ny = y;
			
			for (int s = 0; s < k; s++) {
				nx += dx[d];
				ny += dy[d];
				
				// 범위를 벗어나거나 벽인 경우 제초제 전파 종료 -> 벽인 경우에는 번식할 때 보지 않으므로 같이 넣어줌
				if (!isRange(nx, ny) || map[nx][ny] == -1) {
					break;
				}
				
				// 빈 칸인 경우 번식할 때 빈 칸과 제초제를 뿌린 칸을 보기 때문에 경우를 따로 빼줌
				if (map[nx][ny] == 0) {
					die[nx][ny] = c;
					break;
				}
				
				map[nx][ny] = 0;
				die[nx][ny] = c;
			}
		}
	}
	
	// 제초제가 남아있는 년수 감소
	private static void remove() {
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (die[i][j] > 0) {
					die[i][j]--;
				}
			}
		}
	}
	
	private static boolean isRange(int x, int y) {
		return x >= 0 && x < n && y >= 0 && y < n;
	}

}
