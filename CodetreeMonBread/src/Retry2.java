/**
 * 코드트리 빵
 * 
 * @author minchae
 * @date 2024. 4. 6.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

public class Retry2 {
	
	static class Node implements Comparable<Node> {
		int x;
		int y;
		int dir;
		int dist;
		
		public Node(int x, int y) {
			this.x = x;
			this.y = y;
		}
		
		public Node(int x, int y, int dist) {
			this.x = x;
			this.y = y;
			this.dist = dist;
		}
		
		public Node(int x, int y, int dir, int dist) {
			this.x = x;
			this.y = y;
			this.dir = dir;
			this.dist = dist;
		}
		
		public boolean isSame(Node node) {
			return this.x == node.x && this.y == node.y;
		}

		@Override
		public int compareTo(Node o) {
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
	static int[][] map; // 0 빈 공간, 1 베이스캠프, 2 지나갈 수 없는 곳
	
	static Node[] person;
	static Node[] store;
	
	static int time;

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		n = Integer.parseInt(st.nextToken());
		m = Integer.parseInt(st.nextToken());
		
		map = new int[n][n];
		person = new Node[m];
		store = new Node[m];
		
		for (int i = 0; i < n; i++) {
			st = new StringTokenizer(br.readLine());
			
			for (int j = 0; j < n; j++) {
				map[i][j] = Integer.parseInt(st.nextToken());
			}
		}

		for (int i = 0; i < m; i++) {
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
				moveBasecamp(store[time - 1]);	
			}
			
			if (isFinish()) {
				break;
			}
			
			time++;
		}
		
		System.out.println(time);
	}
	
	private static void moveStore() {
		for (int i = 0; i < m; i++) {
			Node start = person[i];
			Node end = store[i];
			
			// 격자 밖에 있거나 편의점에 도착한 경우 다음으로 넘어감
			if (!isRange(start.x, start.y) || start.isSame(end)) {
				continue;
			}
			
			int dir = findDir(start, end);
			
			start.x += dx[dir];
			start.y += dy[dir];
		}
		
		// 모두 움직인 후에 편의점에 도착했는지 확인
		for (int i = 0; i < m; i++) {
			if (person[i].isSame(store[i])) { 
				map[person[i].x][person[i].y] = 2; // 지나갈 수 없는 곳으로 변경
			}
		}
	}
	
	// 가고싶은 편의점까지 최단거리가 되는 방향을 구함
	private static int findDir(Node start, Node end) {
		PriorityQueue<Node> pq = new PriorityQueue<>();
		boolean[][] visited = new boolean[n][n];
		
		pq.add(new Node(start.x, start.y, -1, 0));
		visited[start.x][start.y] = true;
		
		while (!pq.isEmpty()) {
			Node cur = pq.poll();
			
			// 편의점으로 가는 최단거리가 여러 개가 될수도 있음
			if (cur.isSame(end)) {
				return cur.dir;
			}
			
			for (int i = 0; i < 4; i++) {
				int nx = cur.x + dx[i];
				int ny = cur.y + dy[i];
				
				if (!isRange(nx, ny) || visited[nx][ny] || map[nx][ny] == 2) {
					continue;
				}
				
				pq.add(new Node(nx, ny, cur.dir == -1 ? i : cur.dir, cur.dist + 1));
				visited[nx][ny] = true;
			}
		}
		
		return 0;
	}
	
	private static void moveBasecamp(Node start) {
		PriorityQueue<Node> pq = new PriorityQueue<>();
		boolean[][] visited = new boolean[n][n];
		
		pq.add(new Node(start.x, start.y, 0));
		visited[start.x][start.y] = true;
		
		while (!pq.isEmpty()) {
			Node cur = pq.poll();
			
			// 최단거리가 같은 베이스캠프가 여러 개 있을 수도 있음
			if (map[cur.x][cur.y] == 1) {
				map[cur.x][cur.y] = 2;
				person[time - 1] = cur;
				
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
	
	// 모든 사람이 편의점에 도착했는지 확인
	private static boolean isFinish() {
		for (int i = 0; i < m; i++) {
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
