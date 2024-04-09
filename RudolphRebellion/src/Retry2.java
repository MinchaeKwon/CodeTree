/**
 * 루돌프의 반란
 * 
 * @author minchae
 * @date 2024. 4. 10.
 */

import java.io.*;
import java.util.*;

public class Retry2 {
	
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
	
	static int[][] map; // 루돌프, 산타 위치 저장
	
	static Node[] santa;
	static int[] stun;
	static int[] score;
	static boolean[] dead;
	
	static int rx, ry; // 루돌프 위치

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        P = Integer.parseInt(st.nextToken());
        C = Integer.parseInt(st.nextToken());
        D = Integer.parseInt(st.nextToken());
        
        map = new int[N][N];
        santa = new Node[P + 1];
        stun = new int[P + 1];
        score = new int[P + 1];
        dead = new boolean[P + 1];
        
        st = new StringTokenizer(br.readLine());
        
        rx = Integer.parseInt(st.nextToken()) - 1;
        ry = Integer.parseInt(st.nextToken()) - 1;
        
        map[rx][ry] = -1;
        
        for (int i = 1; i <= P; i++) {
        	st = new StringTokenizer(br.readLine());
        	
        	int n = Integer.parseInt(st.nextToken());
        	int x = Integer.parseInt(st.nextToken()) - 1;
        	int y = Integer.parseInt(st.nextToken()) - 1;
        	
        	santa[n] = new Node(x, y);
        	map[x][y] = n;
        }
        
        while (M-- > 0) {
        	if (isFinish()) {
        		break;
        	}
        	
        	moveRudolph();
        	moveSanta();
        	
        	addScore();
        	decreaseStun();
        }
        
        for (int i = 1; i <= P; i++) {
        	System.out.print(score[i] + " ");
        }
	}
	
	private static void moveRudolph() {
		Node s = findSanta(); // 가까이 있는 산타
		
		// 이동할 방향
		int moveX = 0;
		int moveY = 0;
		
		map[rx][ry] = 0; // 루돌프가 있던 곳 빈 칸으로 만듦
		
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
		
		// 루돌프가 이동할 곳의 위치
		rx += moveX;
		ry += moveY;
		
		map[rx][ry] = -1; // 루돌프 위치 갱신
		
		// 산타와 충돌한 경우 -> 상호작용 일어남
		if (rx == s.x && ry == s.y) {
			// 충돌한 산타 점수 증가, 기절시킴
			score[s.n] += C;
			stun[s.n] = 2;
			
			// 산타가 이동할 위치 -> 루돌프가 이동한 방향으로 튕김
			int nx = s.x + moveX * C;
			int ny = s.y + moveY * C;
			
			// 상호작용
			interaction(s.n, nx, ny, moveX, moveY);
		}
	}
	
	// 루돌프와 가장 가까이에 있는 산타 찾기
	private static Node findSanta() {
		ArrayList<Node> list = new ArrayList<>();
		
		// 각 산타와 루돌프와의 거리를 구함
		for (int i = 1; i <= P; i++) {
			// 탈락한 산타는 넘어감
			if (dead[i]) {
				continue;
			}
			
			Node s = santa[i];
			
			int d = (int) (Math.pow(rx - s.x, 2) + Math.pow(ry - s.y, 2));
			list.add(new Node(i, s.x, s.y, d));
		}
		
		Collections.sort(list);
		
		return list.get(0);
	}
	
	private static void moveSanta() {
		for (int i = 1; i <= P; i++) {
			// 탈락했거나 기절한 산타는 움직이지 않음
			if (dead[i] || stun[i] > 0) {
				continue;
			}
			
			Node cur = santa[i];
			
			// 현재 위치에서 루돌프까지의 거리가 최소 거리
			// 4방향 중 빈 칸이 있어서 이동 가능하지만 루돌프와 더 멀어지는 경우가 있는데 이때는 움직이지 않아야 함
			int min = (int) (Math.pow(rx - cur.x, 2) + Math.pow(ry - cur.y, 2));
			int dir = -1;
			
			// 방향 우선순위인 상우하좌 확인하면서 루돌프와 가장 가까워질 수 있는 방향 찾음
			for (int d = 0; d < 4; d++) {
				// 산타가 이동할 칸
				int nx = cur.x + dx[d];
				int ny = cur.y + dy[d];
				
				// 해당 칸으로 이동할 수 없는 경우 다음 방향 탐색
				if (!isRange(nx, ny) || map[nx][ny] > 0) {
					continue;
				}
				
				int dist = (int) (Math.pow(rx - nx, 2) + Math.pow(ry - ny, 2));
				
				if (dist < min) {
					dir = d;
					min = dist;
				}
			}
			
			// 가장 가까워지는 방향을 찾은 경우 (상우하좌 다 움직였는데 거리가 가까워지지 않는 경우에는 움직이지 않음)
			if (dir != -1) {
				map[cur.x][cur.y] = 0; // 원래 산타가 있던 곳은 빈 칸으로 만듦
				
				// 산타 위치 변경
				cur.x += dx[dir];
				cur.y += dy[dir];
				
				// 이동한 칸에 루돌프가 있는 경우
				if (rx == cur.x && ry == cur.y) {
					// 충돌한 산타 점수 증가, 기절시킴
					score[i] += D;
					stun[i] = 2;
					
					// 산타가 이동할 위치 -> 자신이 온 방향의 반대 방향으로 이동
					int nx = cur.x + (-dx[dir] * D);
					int ny = cur.y + (-dy[dir] * D);
					
					// 상호작용
					interaction(i, nx, ny, -dx[dir], -dy[dir]);
				} else {
					map[cur.x][cur.y] = i;
				}
				
				// 산타가 이동할 곳의 위치
//				int nx = cur.x + dx[dir];
//				int ny = cur.y + dy[dir];
//				
//				// 이동한 칸에 루돌프가 있는 경우
//				if (rx == nx && ry == ny) {
//					// 충돌한 산타 점수 증가, 기절시킴
//					score[i] += D;
//					stun[i] = 2;
//					
//					// 산타가 이동할 위치 -> 자신이 온 방향의 반대 방향으로 이동
//					nx += -dx[dir] * D;
//					ny += -dy[dir] * D;
//					
//					// 상호작용
//					interaction(i, nx, ny, -dx[dir], -dy[dir]);
//				} else {
//					map[nx][ny] = i;
//					santa[i] = new Node(nx, ny);
//				}
			}
		}
	}
	
	// 이동하려는 곳에 다른 산타가 있는 경우 해당 산타 한 칸 뒤로 밀림
	private static void interaction(int n, int x, int y, int moveX, int moveY) {
		if (isRange(x, y)) {
			// 이동하려는 칸에 다른 산타가 있는 경우
			if (map[x][y] > 0) {
				// 다음 칸으로 이동시킴
				interaction(map[x][y], x + moveX, y + moveY, moveX, moveY);
			}

			map[x][y] = n; // 현재 산타는 해당 위치로 옮김
			santa[n] = new Node(x, y);
		} else {
			dead[n] = true;
		}
	}
	
	private static void decreaseStun() {
		for (int i = 1; i <= P; i++) {
			if (stun[i] > 0) {
				stun[i]--;
			}
		}
	}
	
	// 매 턴 이후 아직 탈락하지 않은 산타들에게는 1점씩을 추가로 부여
	private static void addScore() {
		for (int i = 1; i <= P; i++) {
			if (!dead[i]) {
				score[i]++;
			}
		}
	}
	
	private static boolean isFinish() {
		for (int i = 1; i <= P; i++) {
			// 한 명이라도 격자 안인 경우 종료 X
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
