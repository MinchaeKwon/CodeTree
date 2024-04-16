/**
 * 원자 충돌
 * 
 * @author minchae
 * @date 2024. 4. 16.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

public class Retry1 {
	
	static class Node {
		int x;
		int y;
		int m;
		int s;
		int d;
		
		public Node(int x, int y, int m, int s, int d) {
			this.x = x;
			this.y = y;
			this.m = m;
			this.s = s;
			this.d = d;
		}
	}
	
	// ↑, ↗, →, ↘, ↓, ↙, ←, ↖
	static int[] dx = {-1, -1, 0, 1, 1, 1, 0, -1};
	static int[] dy = {0, 1, 1, 1, 0, -1, -1, -1};
	
	static int n, m, k;
	
	static Queue<Node> q = new LinkedList<>();
	static ArrayList<Node> map[][];

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		n = Integer.parseInt(st.nextToken());
		m = Integer.parseInt(st.nextToken());
		k = Integer.parseInt(st.nextToken());
		
		map = new ArrayList[n][n];
		
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				map[i][j] = new ArrayList<>();
			}
		}
		
		for (int i = 0; i < m; i++) {
			st = new StringTokenizer(br.readLine());
			
			int x = Integer.parseInt(st.nextToken()) - 1;
			int y = Integer.parseInt(st.nextToken()) - 1;
			int m = Integer.parseInt(st.nextToken());
			int s = Integer.parseInt(st.nextToken());
			int d = Integer.parseInt(st.nextToken());
			
			q.add(new Node(x, y, m, s, d));
		}

		while (k-- > 0) {
			move();
			sum();
		}
		
		int answer = 0;
		
		while (!q.isEmpty()) {
			answer += q.poll().m;
		}
		
		System.out.println(answer);
	}
	
	private static void move() {
		while (!q.isEmpty()) {
			Node cur = q.poll();
			
			cur.x = (n + cur.x + dx[cur.d] * (cur.s % n)) % n;
			cur.y = (n + cur.y + dy[cur.d] * (cur.s % n)) % n;
			
			map[cur.x][cur.y].add(cur);
		}
	}
	
	private static void sum() {
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (map[i][j].size() >= 2) {
					int massSum = 0;
					int speedSum = 0;
					
					boolean odd = true;
					boolean even = true;
					
					for (Node cur : map[i][j]) {
						massSum += cur.m;
						speedSum += cur.s;
						
						if (cur.d % 2 == 0) {
							odd = false;
						} else {
							even = false;
						}
					}
					
					int mass = massSum / 5;
					
					if (mass > 0) {
						int speed = speedSum / map[i][j].size();
						int start = odd || even ? 0 : 1;
						
						for (int k = start; k < 8; k += 2) {
							q.add(new Node(i, j, mass, speed, k));
						}
					}
				} else {
					q.addAll(map[i][j]);
				}
				
				map[i][j].clear();
			}
		}
	}

}
