/**
 * 나무박멸
 * 삼성 SW 역량테스트 2022 상반기 오후 2번 문제
 * https://www.codetree.ai/training-field/frequent-problems/problems/tree-kill-all/description?page=1&pageSize=20
 * 
 * @author minchae
 * @date 2023. 10. 19.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {
	
	// 상하좌우
	static int[] dx = {-1, 1, 0, 0, -1, -1, 1, 1};
	static int[] dy = {0, 0, -1, 1, -1, 1, -1, 1};
	
	static int n, m, k, c;
	
	static int[][] map; // 빈 칸은 0, 벽은 -1
	static int[][] die;
	
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
        
        // m년동안 진행
        while (m-- > 0) {  	
        	grow();
        	breeding(m);
        	delete();
        	pick();
        }
        
        System.out.println(result);
	}
	
	// 나무 성장
	private static void grow() {
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				// 나무가 있는 경우
				if (map[i][j] > 0) {
					int cnt = 0;
					
					for (int d = 0; d < 4; d++) {
						int nx = i + dx[d];
						int ny = j + dy[d];
						
						// 범위를 벗어나지 않고 나무가 있는 경우
						if (checkRange(nx, ny) && map[nx][ny] > 0) {
							cnt++;
						}
					}
					
					map[i][j] += cnt;
				}
			}
		}
	}
	
	// 나무 번식
	private static void breeding(int p) {
		int[][] newMap = new int[n][n]; // 번식은 모든 나무에서 동시에 일어나기 때문에 맵을 복사해서 사용
		
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (map[i][j] > 0) {
					int cnt = 0;
					
					for (int d = 0; d < 4; d++) {
						int nx = i + dx[d];
						int ny = j + dy[d];
						
						if (checkRange(nx, ny) && die[nx][ny] == 0 && map[nx][ny] == 0) {
							cnt++;
						}
					}
					
					for (int d = 0; d < 4; d++) {
						int nx = i + dx[d];
						int ny = j + dy[d];
						
						if (checkRange(nx, ny) && die[nx][ny] == 0 && map[nx][ny] == 0) {
							newMap[nx][ny] += map[i][j] / cnt;
						}
					}
				}
			}
		}
		
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				map[i][j] += newMap[i][j];
			}
		}
	}
	
	// 제초제를 뿌릴 위치 선정
	private static void pick() {
		int maxCnt = 0;
		int maxX = 0;
		int maxY = 0;
		
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (map[i][j] > 0) {
					int cnt = map[i][j];
					
					// 대각선 방향으로 제초제 전파
					for (int d = 4; d < 8; d++) {
						int nx = i;
						int ny = j;
						
						for (int s = 0; s < k; s++) {
							nx += dx[d];
							ny += dy[d];
							
							// 범위를 벗어나거나 빈칸 또는 벽이 있는 경우에는 제초제 전파가 되지 않기 때문에 break
							if (!checkRange(nx, ny) || map[nx][ny] <= 0) {
								break;
							}
							
							cnt += map[nx][ny]; // 나무가 있는 경우에만 카운트 함
						}
					}
					
					if (maxCnt < cnt) {
						maxX = i;
						maxY = j;
						maxCnt = cnt;
					}
				}
			}
		}
		
		sprinkle(maxX, maxY, maxCnt);
	}
	
	// 제초제 뿌리기
	private static void sprinkle(int x, int y, int cnt) {
		result += cnt;
		
		// 제초제를 뿌릴 곳이 나무인 경우
		if (map[x][y] > 0) {
			map[x][y] = 0;
			die[x][y] = c;
            
            for (int d = 4; d < 8; d++) {
            	int nx = x;
            	int ny = y;
            	
    			for (int s = 0; s < k; s++) {
    				nx += dx[d];
    				ny += dy[d];
    				
    				if (!checkRange(nx, ny) || map[nx][ny] < 0) {
    					break;
    				}
    				
    				// 전파되는 도중 빈칸인 경우 해당 칸까지는 제초제가 전파됨
    				if (map[nx][ny] == 0) {
    					die[nx][ny] = c;
    					break;
    				}
    				
    				map[nx][ny] = 0;
    				die[nx][ny] = c;
    			}
    		}
            
        }
		
	}
	
	// 제초제 기간 감소
	private static void delete() {
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (die[i][j] > 0) {
					die[i][j] -= 1;
				}
			}
    	}
	}
	
	private static boolean checkRange(int x, int y) {
		return x >= 0 && x < n && y >= 0 && y < n;
	}

}
