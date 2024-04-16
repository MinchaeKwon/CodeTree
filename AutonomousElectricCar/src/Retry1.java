/**
 * 자율주행 전기차
 * 
 * @author minchae
 * @date 2024. 4. 17.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

public class Retry1 {
	
	static class Node implements Comparable<Node> {
		int x;
		int y;
		int dist;
		
		public Node (int x, int y) {
			this.x = x;
			this.y = y;
		}
		
		public Node (int x, int y, int dist) {
			this.x = x;
			this.y = y;
			this.dist = dist;
		}

		@Override
		public int compareTo(Retry1.Node o) {
			if (this.dist == o.dist) {
				if (this.x == o.x) {
					return this.y - o.y;
				}
				
				return this.x - o.x;
			}
			
			return this.dist - o.dist;
		}
	}
	
	// 상하좌우
	static int[] dx = {-1, 1, 0, 0};
	static int[] dy = {0, 0, -1, 1};
	
	static int n, m, c;
	
	static int[][] map; // 0 도로, -1 벽, 승객 번호
	static int sx, sy;
	static Node[] departure;
	static Node[] destination;

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		n = Integer.parseInt(st.nextToken());
		m = Integer.parseInt(st.nextToken());
		c = Integer.parseInt(st.nextToken());
		
		map = new int[n][n];
		departure = new Node[m + 1];
		destination = new Node[m + 1];
		
		for (int i = 0; i < n; i++) {
			st = new StringTokenizer(br.readLine());
			
			for (int j = 0; j < n; j++) {
				map[i][j] = Integer.parseInt(st.nextToken());
				
				if (map[i][j] == 1) {
					map[i][j] = -1;
				}
			}
		}
		
		st = new StringTokenizer(br.readLine());
		
		sx = Integer.parseInt(st.nextToken()) - 1;
		sy = Integer.parseInt(st.nextToken()) - 1;
		
		for (int i = 1; i <= m; i++) {
			st = new StringTokenizer(br.readLine());
			
			int x1 = Integer.parseInt(st.nextToken()) - 1;
			int y1 = Integer.parseInt(st.nextToken()) - 1;
			int x2 = Integer.parseInt(st.nextToken()) - 1;
			int y2 = Integer.parseInt(st.nextToken()) - 1;
			
			departure[i] = new Node(x1, y1);
			destination[i] = new Node(x2, y2);
			
			map[x1][y1] = i; // 승객 번호 저장
		}

		for (int i = 1; i <= m; i++) {
			int num = find();
			
			if (num == -1) {
				System.out.println(-1);
				return;
			}
			
			// 자동차 위치 갱신
			sx = departure[num].x;
			sy = departure[num].y;
			
			if (goToEnd(destination[num])) {
				// 목적지까지 갔으니까 위치 갱신
				sx = destination[num].x;
				sy = destination[num].y;
			} else {
				System.out.println(-1);
				return;
			}
		}
		
		System.out.println(c);
	}
	
	// 태울 승객 찾기
	private static int find() {
		PriorityQueue<Node> pq = new PriorityQueue<>();
		boolean[][] visited = new boolean[n][n];
		
		// 자동차 위치에서 시작
		pq.add(new Node(sx, sy, 0));
		visited[sx][sy] = true;
		
		while (!pq.isEmpty()) {
			Node cur = pq.poll();
			
			if (map[cur.x][cur.y] > 0) {
				int num = map[cur.x][cur.y]; // 승객 번호
				map[cur.x][cur.y] = 0;
				
				c -= cur.dist;
				
				return c >= 0 ? num : -1; // 연료가 부족하면 -1 반환
			}
			
			for (int i = 0; i < 4; i++) {
				int nx = cur.x + dx[i];
				int ny = cur.y + dy[i];
				
				if (!isRange(nx, ny) || visited[nx][ny] || map[nx][ny] == -1) {
					continue;
				}
				
				pq.add(new Node(nx, ny, cur.dist + 1));
				visited[nx][ny] = true;
			}
		}
		
		return -1;
	}
	
	private static boolean goToEnd(Node end) {
		PriorityQueue<Node> pq = new PriorityQueue<>();
		boolean[][] visited = new boolean[n][n];
		
		// 자동차 위치에서 시작
		pq.add(new Node(sx, sy, 0));
		visited[sx][sy] = true;
		
		while (!pq.isEmpty()) {
			Node cur = pq.poll();
			
			if (cur.x == end.x && cur.y == end.y) {
				c -= cur.dist;
				
				if (c >= 0) {
					c += cur.dist * 2;
					return true;
				} else {
					return false;
				}
			}
			
			for (int i = 0; i < 4; i++) {
				int nx = cur.x + dx[i];
				int ny = cur.y + dy[i];
				
				if (!isRange(nx, ny) || visited[nx][ny] || map[nx][ny] == -1) {
					continue;
				}
				
				pq.add(new Node(nx, ny, cur.dist + 1));
				visited[nx][ny] = true;
			}
		}
		
		return false;
	}
	
	private static boolean isRange(int x, int y) {
		return x >= 0 && x < n && y >= 0 && y < n;
	}

}
