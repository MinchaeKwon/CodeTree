/**
 * 코드트리 빵
 * 
 * @author minchae
 * @date 2024. 4. 18.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

public class Retry3 {
	
	static class Node implements Comparable<Node> {
		int x;
		int y;
		int dist;
		int dir;
		
		public Node(int x, int y) {
			this.x = x;
			this.y = y;
		}
		
		public Node(int x, int y, int dist) {
			this.x = x;
			this.y = y;
			this.dist = dist;
		}
		
		public Node(int x, int y, int dist, int dir) {
			this.x = x;
			this.y = y;
			this.dist = dist;
			this.dir = dir;
		}
		
		public boolean isSame(Node node) {
			return this.x == node.x && this.y == node.y;
		}
		
		@Override
		public int compareTo(Retry3.Node o) {
			if (this.dist == o.dist) {
				if (this.x == o.x) {
					return this.y - o.y;
				}
				
				return this.x - o.x;
			}
			
			return this.dist - o.dist;
		}
	}
	
	// 상좌우하
	static int[] dx = {-1, 0, 0, 1};
	static int[] dy = {0, -1, 1, 0};
	
	static int n, m;
	
	static int[][] map; // 0 빈 칸, 1 베이스캠프, 2 지나갈 수 없는 곳
	static Node[] person;
	static Node[] store;
	
	static int time;

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		n = Integer.parseInt(st.nextToken());
		m = Integer.parseInt(st.nextToken());
		
		map = new int[n][n];
		person = new Node[m + 1];
		store = new Node[m + 1];
		
		for (int i = 0; i < n; i++) {
			st = new StringTokenizer(br.readLine());
			
			for (int j = 0; j < n; j++) {
				map[i][j] = Integer.parseInt(st.nextToken());
			}
		}
		
		for (int i = 1; i <= m; i++) {
			st = new StringTokenizer(br.readLine());
			
			int x = Integer.parseInt(st.nextToken()) - 1;
			int y = Integer.parseInt(st.nextToken()) - 1;
			
			store[i] = new Node(x, y);
			person[i] = new Node(-1, -1);
		}

		time = 1;
		
		while (true) {
			moveStore();

			if (time <= m) {
				moveBasecamp(store[time]);
			}

			// 편의점으로 이동한 후에 모든 사람이 다 도착했는지 확인
			if (isFinish()) {
				break;
			}
			
			time++;
		}
		
		System.out.println(time);
	}
	
	private static void moveStore() {
		for (int i = 1; i <= m; i++) {
			Node start = person[i];
			Node end = store[i];
			
			if (!isRange(start.x, start.y) || start.isSame(end)) {
				continue;
			}
			
			int dir = findDir(start, end);
			
			start.x += dx[dir];
			start.y += dy[dir];
		}
		
		for (int i = 1; i <= m; i++) {
			if (person[i].isSame(store[i])) {
				map[person[i].x][person[i].y] = 2;
			}
		}
	}
	
	private static int findDir(Node start, Node end) {
		PriorityQueue<Node> pq = new PriorityQueue<>();
		boolean[][] visited = new boolean[n][n];
		
		pq.add(new Node(start.x, start.y, 0, -1));
		visited[start.x][start.y] = true;
		
		while (!pq.isEmpty()) {
			Node cur = pq.poll();
			
			if (cur.isSame(end)) {
				return cur.dir;
			}
			
			for (int i = 0; i < 4; i++) {
				int nx = cur.x + dx[i];
				int ny = cur.y + dy[i];
				
				if (!isRange(nx, ny) || visited[nx][ny] || map[nx][ny] == 2) {
					continue;
				}
				
				pq.add(new Node(nx, ny, cur.dist + 1, cur.dir == -1 ? i : cur.dir));
				visited[nx][ny] = true;
			}
		}
		
		return -1;
	}
	
	private static void moveBasecamp(Node start) {
		PriorityQueue<Node> pq = new PriorityQueue<>();
		boolean[][] visited = new boolean[n][n];
		
		pq.add(new Node(start.x, start.y, 0));
		visited[start.x][start.y] = true;
		
		while (!pq.isEmpty()) {
			Node cur = pq.poll();
			
			if (map[cur.x][cur.y] == 1) {
				map[cur.x][cur.y] = 2;
				person[time] = cur;
				
				return;
			}
			
			for (int i = 0; i < 4; i++) {
				int nx = cur.x + dx[i];
				int ny = cur.y + dy[i];
				
				if (!isRange(nx, ny) || visited[nx][ny] || map[nx][ny] == 2) {
					continue;
				}
				
				pq.add(new Node(nx, ny, cur.dist + 1));
				visited[nx][ny] = true;
			}
		}
	}
	
	private static boolean isFinish() {
		for (int i = 1; i <= m; i++) {
			if (!person[i].isSame(store[i])) {
				return false;
			}
		}
		
		return true;
	}
	
	private static boolean isRange(int x, int y) {
		return x >= 0 && x < n && y >= 0 && y < n;
	}

}
