/**
 * 나무박멸
 * 22 상 오후 2번
 *
 * @author minchae
 * @date 2024. 9. 27. 00:00 ~ 00:50
 * 
 * 메모리 17MB  시간 211ms
 */

import java.io.*;
import java.util.*;

public class TreeKillAll {
	
	static class Pair {
		int x;
		int y;
		
		public Pair(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}
	
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
			breeding();
			decrease();
			find();
		}
		
		System.out.println(answer);
	}
	
	// 나무 성장
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
	
	// 나무 번식
	private static void breeding() {
		int[][] copy = new int[n][n];
		
		for (int i = 0; i < n; i++) {
			copy[i] = Arrays.copyOf(map[i], n);
		}
		
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (map[i][j] <= 0) {
					continue;
				}
				
				ArrayList<Pair> list = new ArrayList<>();
				
				for (int d = 0; d < 4; d++) {
					int nx = i + dx[d];
					int ny = j + dy[d];
					
					if (isRange(nx, ny) && map[nx][ny] == 0 && dead[nx][ny] == 0) {
						list.add(new Pair(nx, ny));
					}
				}
				
				int cnt = list.size();
				
				for (Pair cur : list) {
					copy[cur.x][cur.y] += map[i][j] / cnt;
				}
			}
		}
		
		map = copy;
	}
	
	// 제초제 남은 수 감소
	private static void decrease() {
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (dead[i][j] > 0) {
					dead[i][j]--;
				}
			}
		}
	}
	
	// 가장 많이 박멸하는 곳 찾기
	private static void find() {
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
					
					for (int s = 0; s < k; s++) {
						nx += dx[d];
						ny += dy[d];
						
						if (!isRange(nx, ny) || map[nx][ny] <= 0) {
							break;
						}
						
						cnt += map[nx][ny];
					}
				}
				
				if (cnt > maxCnt) {
					maxCnt = cnt;
					maxX = i;
					maxY = j;
				}
			}
		}
		
		answer += maxCnt;
		spread(maxX, maxY);
	}
	
	private static void spread(int x, int y) {
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
				
				dead[nx][ny] = c;
				
				// 빈 칸인 경우 해당 칸까지는 제초제 퍼짐 -> 그 이후로는 퍼지지 않기 때문에 break
				if (map[nx][ny] == 0) {
					break;
				}
				
				map[nx][ny] = 0;
			}
		}
	}
	
	private static boolean isRange(int x, int y) {
		return x >= 0 && x < n && y >= 0 && y < n;
	}

}
