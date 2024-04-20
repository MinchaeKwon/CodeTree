/**
 * 나무박멸
 * 
 * @author minchae
 * @date 2024. 4. 20.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer;

public class Retry2 {
	
	static class Pair {
		int x;
		int y;
		
		public Pair(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}
	
	// 상하좌우, 대각선
	static int[] dx = {-1, 1, 0, 0, -1, -1, 1, 1};
	static int[] dy = {0, 0, -1, 1, -1, 1, -1, 1};
	
	static int n, m, k, c;
	
	static int[][] map;
	static int[][] dead;
	
	static int answer;

	public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        k = Integer.parseInt(st.nextToken());
        c = Integer.parseInt(st.nextToken());
        
        map = new int[n][n];
        dead = new int[n][n];
        
        for (int i = 0; i < n; i++) {
        	st = new StringTokenizer(br.readLine());
        	
        	for (int j = 0; j < n; j++) {
        		map[i][j] = Integer.parseInt(st.nextToken());
        	}
        }
        
        while (m-- > 0) {
        	grow();
        	spread();
        	decrease();
        	pick();
        }

        System.out.println(answer);
	}
	
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
	
	private static void spread() {
		int[][] copy = new int[n][n];
		
		for (int i = 0; i < n; i++) {
			copy[i] = Arrays.copyOf(map[i], n);
		}
		
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (map[i][j] <= 0) {
					continue;
				}
				
				int cnt = 0;
				ArrayList<Pair> list = new ArrayList<>();
				
				for (int d = 0; d < 4; d++) {
					int nx = i + dx[d];
					int ny = j + dy[d];
					
					if (isRange(nx, ny) && map[nx][ny] == 0 && dead[nx][ny] == 0) {
						cnt++;
						list.add(new Pair(nx, ny));
					}
				}
				
				for (Pair cur : list) {
					copy[cur.x][cur.y] += map[i][j] / cnt;
				}
			}
		}
		
		map = copy;
	}
	
	private static void pick() {
		int max = 0;
		int maxX = 0;
		int maxY = 0;
		
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (map[i][j] <= 0) {
					continue;
				}
				
				int cnt = map[i][j];
				
				for (int d = 4; d < 8; d++) {
					int nx = i;
					int ny = j;
					
					for (int s = 0; s < k; s++) {
						nx += dx[d];
						ny += dy[d];
						
						if (!isRange(nx, ny) || map[nx][ny] <= 0) {
							break;
						}
						
						cnt += map[nx][ny];
					}
				}
				
				if (cnt > max) {
					max = cnt;
					maxX = i;
					maxY = j;
				}
			}
		}
		
		sprinkle(maxX, maxY, max);
	}
	
	private static void sprinkle(int x, int y, int cnt) {
		answer += cnt;
		
		map[x][y] = 0;
		dead[x][y] = c;
		
		for (int d = 4; d < 8; d++) {
			int nx = x;
			int ny = y;
			
			for (int s = 0; s < k; s++) {
				nx += dx[d];
				ny += dy[d];
				
				if (!isRange(nx, ny) || map[nx][ny] == -1) {
					break;
				}
				
				if (map[nx][ny] == 0) {
					dead[nx][ny] = c;
					break;
				}
				
				map[nx][ny] = 0;
				dead[nx][ny] = c;
			}
		}
	}
	
	private static void decrease() {
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (dead[i][j] > 0) {
					dead[i][j]--;
				}
			}
		}
	}
	
	private static boolean isRange(int x, int y) {
		return x >= 0 && x < n && y >= 0 && y < n;
	}

}
