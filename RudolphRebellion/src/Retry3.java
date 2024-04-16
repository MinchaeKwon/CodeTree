/**
 * 루돌프의 반란
 * 
 * @author minchae
 * @date 2024. 4. 17.
 */

import java.io.*;
import java.util.*;

public class Retry3 {
	
	static class Node implements Comparable<Node> {
		int n;
		int x;
		int y;
		int d;
		
		public Node(int x, int y) {
			this.x = x;
			this.y = y;
		}
		
		public Node(int n, int x, int y, int d) {
			this.n = n;
			this.x = x;
			this.y = y;
			this.d = d;
		}

		@Override
		public int compareTo(Node o) {
			if (this.d == o.d) {
				if (this.x == o.x) {
					return o.y - this.y;
				}
				
				return o.x - this.x;
			}
			
			return this.d - o.d;
		}
	}
	
	// 상우하좌
	static int[] dx = {-1, 0, 1, 0};
	static int[] dy = {0, 1, 0, -1};
	
	static int N, M, P, C, D;
	
	static Node[] santa;
	static int[] stun;
	static boolean[] dead;
	
	static int[][] map; // 루돌프, 산타 위치 저장
	
	static int rx, ry;
	
	static int[] score;

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        
        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        P = Integer.parseInt(st.nextToken());
        C = Integer.parseInt(st.nextToken());
        D = Integer.parseInt(st.nextToken());
        
        map = new int[N][N];
        
        st = new StringTokenizer(br.readLine());
        
        rx = Integer.parseInt(st.nextToken()) - 1;
        ry = Integer.parseInt(st.nextToken()) - 1;
        
        santa = new Node[P + 1];
        stun = new int[P + 1];
        dead = new boolean[P + 1];
        score = new int[P + 1];
        
        for (int i = 0; i < P; i++) {
        	st = new StringTokenizer(br.readLine());
        	
        	int num = Integer.parseInt(st.nextToken());
        	int x = Integer.parseInt(st.nextToken()) - 1;
        	int y = Integer.parseInt(st.nextToken()) - 1;
        	
        	santa[num] = new Node(x, y);
        }
        
        while (M-- > 0) {
        	if (isFinish()) {
        		break;
        	}
        	
        	moveRudolph();
        	moveSanta();
        	
        	addScore();
        	decrease();
        }
        
        for (int i = 1; i <= P; i++) {
        	System.out.print(score[i] + " ");
        }
	}
	
	private static void moveRudolph() {
		Node s = findSanta();
		
		int moveX = 0;
		int moveY = 0;
		
		if (rx < s.x) {
			moveX = 1;
		} else if (rx > s.x) {
			moveX = -1;
		}
		
		if (ry < s.y) {
			moveY = 1;
		} else if (ry > s.y) {
			moveY = -1;
		}
		
		map[rx][ry] = 0;
		
		rx += moveX;
		ry += moveY;
		
		map[rx][ry] = -1;
		
		if (rx == s.x && ry == s.y) {
			score[s.n] += C;
			stun[s.n] = 2;
			
			// 산타가 이동할 위치
			int nx = s.x + moveX * C;
			int ny = s.y + moveY * C;
			
			interaction(s.n, nx, ny, moveX, moveY);
		}
	}
	
	private static Node findSanta() {
		ArrayList<Node> list = new ArrayList<>();
		
		for (int i = 1; i <= P; i++) {
			if (dead[i]) {
				continue;
			}
			
			int dist = (int) (Math.pow(rx - santa[i].x, 2) + Math.pow(ry - santa[i].y, 2));
			list.add(new Node(i, santa[i].x, santa[i].y, dist));
		}
		
		Collections.sort(list);
		
		return list.get(0);
	}
	
	private static void interaction(int n, int x, int y, int moveX, int moveY)  {
		if (isRange(x, y)) {
			// 해당 위치에 있던 산타는 한 칸 밀림
			if (map[x][y] > 0) {
				interaction(map[x][y], x + moveX, y + moveY, moveX, moveY);
			}
			
			// 산타 위치 갱신
			map[x][y] = n;
			santa[n] = new Node(x, y);
		} else {
			dead[n] = true;
		}
	}
	
	private static void moveSanta() {
		for (int i = 1; i <= P; i++) {
			if (dead[i] || stun[i] > 0) {
				continue;
			}
			
			Node cur = santa[i];
			
			int min = (int) (Math.pow(rx - cur.x, 2) + Math.pow(ry - cur.y, 2));
			int dir = -1;
			
			for (int d = 0; d < 4; d++) {
				int nx = cur.x + dx[d];
				int ny = cur.y + dy[d];
				
				// 범위를 벗어나거나 다른 산타가 있는 경우
				if (!isRange(nx, ny) || map[nx][ny] > 0) {
					continue;
				}
				
				int dist = (int) (Math.pow(rx - nx, 2) + Math.pow(ry - ny, 2));
				
				if (dist < min) {
					min = dist;
					dir = d;
				}
			}
			
			if (dir != -1) {
				map[cur.x][cur.y]  = 0;
				
				cur.x += dx[dir];
				cur.y += dy[dir];
				
				if (cur.x == rx && cur.y == ry) {
					score[i] += D;
					stun[i] = 2;
					
					int nx = cur.x + (-dx[dir] * D);
					int ny = cur.y + (-dy[dir] * D);
					
					interaction(i, nx, ny, -dx[dir], -dy[dir]);
				} else {
					map[cur.x][cur.y] = i;
				}
			}
		}
	}
	
	private static void addScore() {
		for (int i = 1; i <= P; i++) {
			if (!dead[i]) {
				score[i]++;
			}
		}
	}
	
	private static void decrease() {
		for (int i = 1; i <= P; i++) {
			if (stun[i] > 0) {
				stun[i]--;
			}
		}
	}

	private static boolean isFinish() {
		for (int i = 1; i <= P; i++) {
			if (!dead[i]) {
				return false;
			}
		}
		
		return true;
	}
	
	private static boolean isRange(int x, int y) {
		return x >= 0 && x < N && y >= 0 && y < N;
	}
	
}
