/**
 * 색깔 폭탄
 * 
 * @author minchae
 * @date 2024. 4. 3.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.StringTokenizer;

public class Retry1 {
	
	static class Bomb implements Comparable<Bomb> {
		int x;
		int y;
		int size;
		int red;
		
		public Bomb(int x, int y) {
			this.x = x;
			this.y = y;
		}
		
		public Bomb(int x, int y, int size, int red) {
			this.x = x;
			this.y = y;
			this.size = size;
			this.red = red;
		}

		@Override
		public int compareTo(Bomb o) {
			if (this.size == o.size) {
				if (this.red == o.red) {
					if (this.x == o.x) {
						return this.y - o.y;
					}
					
					return o.x - this.x;
				}
				
				return this.red - o.red;
			}
			
			return o.size - this.size;
		}
	}
	
	// 상하좌우
	static int[] dx = {-1, 1, 0, 0};
	static int[] dy = {0, 0, -1, 1};
	
	static int n, m;
	static int[][] map;
	
	static PriorityQueue<Bomb> pq;
	static boolean[][] visited;

	public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        
        map = new int[n][n];
        
        for (int i = 0; i < n; i++) {
        	st = new StringTokenizer(br.readLine());
        	
        	for (int j = 0; j < n; j++) {
        		map[i][j] = Integer.parseInt(st.nextToken());
        	}
        }
        
        int answer = 0;

        while (true) {
        	pq = new PriorityQueue<>();
        	visited = new boolean[n][n];
        	
        	for (int i = 0; i < n; i++) {
        		for (int j = 0; j < n; j++) {
        			if (!visited[i][j] && map[i][j] > 0) {
        				bfs(i, j);
        			}
        		}
        	}
        	
        	if (pq.isEmpty()) {
        		break;
        	}
        	
        	Bomb bomb = pq.poll();
        	remove(bomb);
        	
        	answer += Math.pow(bomb.size, 2);
        	
        	gravity();
        	rotate();
        	gravity();
        }
        
        System.out.println(answer);
	}
	
	// 가장 큰 폭탄 묶음 찾기
	private static void bfs(int x, int y) {
		Queue<Bomb> q = new LinkedList<>();
		
		q.add(new Bomb(x, y));
		visited[x][y] = true;
		
		int cnt = 1;
		int red = 0;
		
		int color = map[x][y];
		
		ArrayList<Bomb> list = new ArrayList<>(); // 기준점을 찾기 위해 사용
		list.add(new Bomb(x, y));
		
		while (!q.isEmpty()) {
			Bomb cur = q.poll();
			
			for (int i = 0; i < 4; i++) {
				int nx = cur.x + dx[i];
				int ny = cur.y + dy[i];
				
				if (!isRange(nx, ny) || visited[nx][ny]) {
					continue;
				}
				
				if (map[nx][ny] == color || map[nx][ny] == 0) {
					if (map[nx][ny] == 0) {
						red++;
					} else {
						list.add(new Bomb(nx, ny));
					}
					
					q.add(new Bomb(nx, ny));
					visited[nx][ny] = true;
					
					cnt++;
				}
			}
		}
		
		if (cnt >= 2) {
			Collections.sort(list);
			Bomb base = list.get(0);
			
			pq.add(new Bomb(base.x, base.y, cnt, red));
		}
		
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (map[i][j] == 0) {
					visited[i][j] = false;
				}
			}
		}
	}
	
	// 폭탄 묶음 제거
	private static void remove(Bomb start) {
		int color = map[start.x][start.y];
		
		Queue<Bomb> q = new LinkedList<>();
		
		q.add(start);
		map[start.x][start.y] = -2;
		
		while (!q.isEmpty()) {
			Bomb cur = q.poll();
			
			for (int i = 0; i < 4; i++) {
				int nx = cur.x + dx[i];
				int ny = cur.y + dy[i];
				
				if (isRange(nx, ny) && (map[nx][ny] == color || map[nx][ny] == 0)) {
					map[nx][ny] = -2;
					q.add(new Bomb(nx, ny));
				}
			}
		}
	}
	
	// 중력 작용
	private static void gravity() {
		for (int j = 0; j < n; j++) {
			for (int i = n - 1; i > 0; i--) {
				if (map[i][j] != -2) {
					continue;
				}
				
				int x = i;
				
				while (true) {
					x -= 1;
					
					// 범위를 벗어나거나 돌이 있는 경우 중력 작용 멈춤
					if (x < 0 || map[x][j] == -1) {
						break;
					}
					
					// 폭탄이 있는 경우
					if (map[x][j] != -2) {
						map[i][j] = map[x][j];
						map[x][j] = -2;
						break;
					}
				}
			}
		}
	}
	
	// 반시계 90도 방향 회전
	private static void rotate() {
		int[][] copy = new int[n][n];
		
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				copy[n - j - 1][i] = map[i][j];
			}
		}
		
		map = copy;
	}
	
	private static boolean isRange(int x, int y) {
		return x >= 0 && x < n && y >= 0 && y < n;
	}

}
