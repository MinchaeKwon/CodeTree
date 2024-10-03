/**
 * 루돌프의 반란
 * 23 하 오후 1번
 *
 * @author minchae
 * @date 2024. 10. 3. 14:03 ~ 15:12
 * */

import java.io.*;
import java.util.*;

public class Rudolph {
	
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
			if (this.d != o.d) {
				return Integer.compare(this.d, o.d);
			}
			
			if (this.x != o.x) {
				return Integer.compare(o.x, this.x);
			}
			
			return Integer.compare(o.y, this.y);
		}
	}
	
	// 상우하좌
	static int[] dx = {-1, 0, 1, 0};
	static int[] dy = {0, 1, 0, -1};
	
	static int N, M, P, C, D;
	
	static int[][] map; // 루돌프 -1, 산타 번호로 위치 저장
	
	static Node[] santa;
	static int[] stun;
	static boolean[] dead;
	static int[] score;
	
	static int rx, ry;

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
		dead = new boolean[P + 1];
		score = new int[P + 1];
		
		st = new StringTokenizer(br.readLine());
		
		rx = Integer.parseInt(st.nextToken()) - 1;
		ry = Integer.parseInt(st.nextToken()) - 1;
		
		map[rx][ry] = -1;
		
		for (int i = 1; i <= P; i++) {
			st = new StringTokenizer(br.readLine());
			
			int n = Integer.parseInt(st.nextToken());
			int x = Integer.parseInt(st.nextToken()) - 1;
			int y = Integer.parseInt(st.nextToken()) - 1;
			
			santa[n] = new Node(n, x, y, 0);
			
			map[x][y] = n;
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
		Node find = findSanta();
		
		int moveX = 0;
		int moveY = 0;
		
		if (rx > find.x) {
			moveX--;
		} else if (rx < find.x) {
			moveX++;
		}
		
		if (ry > find.y) {
			moveY--;
		} else if (ry < find.y) {
			moveY++;
		}
		
		map[rx][ry] = 0; // 원래 루돌프가 있던 자리는 빈 칸으로 만듦
		
		// 루돌프 위치 이동
		rx += moveX;
		ry += moveY;
		
		map[rx][ry] = -1;
		
		// 이동했는데 산타와 충돌하는 경우
		if (rx == find.x && ry == find.y) {
			score[find.n] += C;
			stun[find.n] = 2;
			
			// 밀려나는 산타가 이동할 위치
			int nx = find.x + moveX * C;
			int ny = find.y + moveY * C;
			
			interaction(find.n, nx, ny, moveX, moveY);
		}
	}
	
	// 루돌프와 가장 가까운 산타 찾기
	private static Node findSanta() {
		ArrayList<Node> list = new ArrayList<>();
		
		for (int i = 1; i <= P; i++) {
			if (dead[i]) {
				continue;
			}
			
			int dist = getDist(santa[i].x, santa[i].y);
			
			list.add(new Node(i, santa[i].x, santa[i].y, dist));
		}
		
		Collections.sort(list);
		
		return list.get(0);
	}
	
	private static void moveSanta() {
		for (int i = 1; i <= P; i++) {
			// 탈락하거나 기절한 산타는 움직일 수 없음
			if (dead[i] || stun[i] > 0) {
				continue;
			}
			
			Node cur = santa[i];
			
			int midDist = getDist(cur.x, cur.y);
			int dir = -1;
			
			// 4방향 탐색 -> 가까워지는 방향 찾음
			for (int d = 0; d < 4; d++) {
				int nx = cur.x + dx[d];
				int ny = cur.y + dy[d];
				
				// 범위를 벗어나거나 다른 산타가 있는 경우 움직일 수 없음
				if (!isRange(nx, ny) || map[nx][ny] > 0) {
					continue;
				}
				
				int dist = getDist(nx, ny);
				
				if (dist < midDist) {
					midDist = dist;
					dir = d;
				}
			}
			
			// 가까워지는 방향이 있을 경우
			if (dir != -1) {
				map[cur.x][cur.y] = 0;
				
				cur.x += dx[dir];
				cur.y += dy[dir];
				
				if (cur.x == rx && cur.y == ry) { // 이동했는데 루돌프와 충돌하는 경우
					score[i] += D;
					stun[i] = 2;
					
					// 자신이 이동해온 방향의 반대 방향으로 D칸 이동
					int nx = cur.x + (-dx[dir] * D);
					int ny = cur.y + (-dy[dir] * D);
					
					interaction(i, nx, ny, -dx[dir], -dy[dir]);
				} else {
					// 충돌하지 않으면 위치 갱신
					map[cur.x][cur.y] = i;
				}
			}
		}
	}
	
	// 상호작용
	private static void interaction(int n, int x, int y, int moveX, int moveY) {
		if (isRange(x, y)) { // 범위 안에 있는 경우
			// 이동한 위치에 다른 산타가 있는 경우
			if (map[x][y] > 0) {
				interaction(map[x][y], x + moveX, y + moveY, moveX, moveY);
			}
			
			// 일단 현재 산타는 위치 갱신
			map[x][y] = n;
			
			santa[n].x = x;
			santa[n].y = y;
		} else {
			dead[n] = true; // 밀려난 위치가 범위 밖인 경우 탈락
		}
	}
	
	// 루돌프와 산타의 거리 구하기
	private static int getDist(int x, int y) {
		return (int) (Math.pow(Math.abs(rx - x), 2) + Math.pow(Math.abs(ry - y), 2));
	}
	
	// 탈락하지 않은 산타 점수 1 증가
	private static void addScore() {
		for (int i = 1; i <= P; i++) {
			if (!dead[i]) {
				score[i]++;
			}
		}
	}
	
	// 기절 후 다시 돌아오는 턴 수 감소
	private static void decrease() {
		for (int i = 1; i <= P; i++) {
			if (!dead[i] && stun[i] > 0) {
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
		return x >= 0 && x < N && y >=0 && y < N;
	}
}
