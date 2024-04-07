/**
 * 회전하는 빙하
 * 
 * @author minchae
 * @date 2024. 4. 7.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

public class Retry1 {
	
	static class Pair {
		int x;
		int y;
		
		public Pair(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}
	
	// 우하상좌
	static int[] dx = {0, 1, -1, 0};
	static int[] dy = {1, 0, 0, -1};
	
	static int n, q;
	static int[][] map;
	static int[][] newMap;
	
	static int maxSize;
	static int total;

	public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        
        n = Integer.parseInt(st.nextToken());
        q = Integer.parseInt(st.nextToken());
        
        n = (int) Math.pow(2, n);
        
        map = new int[n][n];
        
        for (int i = 0; i < n; i++) {
        	st = new StringTokenizer(br.readLine());
        	
        	for (int j = 0; j < n; j++) {
        		map[i][j] = Integer.parseInt(st.nextToken());
        	}
        }
        
        st = new StringTokenizer(br.readLine());
        
        while (q-- > 0) {
        	int l = Integer.parseInt(st.nextToken());
        	
        	if (l > 0) {
        		rotateMap(l);
        	}
        	
        	melting();
        }

        bfs();
        
        System.out.println(total);
        System.out.println(maxSize);
	}
	
	private static void rotateMap(int l) {
		newMap = new int[n][n];
		
		// 2^L * 2^L만큼 격자 선택 후 2^(L-1) * 2^(L-1)로 잘라 4등분하여 회전
		
		int size = (int) Math.pow(2, l - 1);
		
		l = (int) Math.pow(2, l);
		
		for (int i = 0; i < n; i += l) {
			for (int j = 0; j < n; j += l) {
				rotate(i, j, size, 0); // 좌상단의 칸부터 회전 -> 우로 움직임
				rotate(i, j + size, size, 1);
				rotate(i + size, j, size, 2);
				rotate(i + size, j + size, size, 3);
			}
		}
		
		map = newMap;
	}
	
	private static void rotate(int sx, int sy, int size, int dir) {
		for (int i = sx; i < sx + size; i++) {
			for (int j = sy; j < sy + size; j++) {
				int nx = i + dx[dir] * size;
				int ny = j + dy[dir] * size;
				
				newMap[nx][ny] = map[i][j];
			}
		}
	}
	
	private static void melting() {
		int[][] copy = new int[n][n];
		
		for (int i = 0; i < n; i++) {
			copy[i] = Arrays.copyOf(map[i], n);
		}
		
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (map[i][j] == 0) {
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
				
				if (cnt <= 2) {
					copy[i][j]--;
				}
			}
		}
		
		map = copy;
	}
	
	private static void bfs() {
		Queue<Pair> q = new LinkedList<>();
		boolean[][] visited = new boolean[n][n];
		
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				total += map[i][j];
				
				if (!visited[i][j] && map[i][j] > 0) {
					q.add(new Pair(i, j));
					visited[i][j] = true;
					
					int cnt = 1;
					
					while (!q.isEmpty()) {
						Pair cur = q.poll();
						
						for (int d = 0; d < 4; d++) {
							int nx = cur.x + dx[d];
							int ny = cur.y + dy[d];
							
							if (isRange(nx, ny) && !visited[nx][ny] && map[nx][ny] > 0) {
								q.add(new Pair(nx, ny));
								visited[nx][ny] = true;
								
								cnt++;
							}
						}
					}
					
					maxSize = Math.max(maxSize, cnt);
				}
			}
		}
	}
	
	private static boolean isRange(int x, int y) {
		return x >= 0 && x < n && y >= 0 && y < n;
	}

}
