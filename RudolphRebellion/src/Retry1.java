/**
 * 루돌프의 반란
 * 
 * @author minchae
 * @date 2024. 3. 18.
 */

import java.io.*;
import java.util.*;

public class Retry1 {
	
	static class Pair {
		int x;
		int y;
		
		public Pair(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}
	
	static class Dist implements Comparable<Dist> {
		int x;
		int y;
		int d;
		int n;
		
		public Dist(int x, int y, int d, int n) {
			this.x = x;
			this.y = y;
			this.d = d;
			this.n = n;
		}

		@Override
		public int compareTo(Retry1.Dist o) {
			if (this.d == o.d) {
				if (this.x == o.x) {
					return o.y - this.y;
				}
				
				return o.x - this.x;
			}
			
			return this.d - o.d;
		}
	}
	
	// 상우하좌, 대각선
	static int[] dx = {-1, 0, 1, 0};
	static int[] dy = {0, 1, 0, -1};
	
	static int N, M, P, C, D;
	static int rx, ry;
	static Pair[] santa;
	
	static int[][] map; // 산타, 루돌프 위치 저장
	static int[] stun;
	static boolean[] dead;
	
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
		santa = new Pair[P + 1];
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
			
			santa[n] = new Pair(x, y);
			map[x][y] = n;
		}
		
		while (M-- > 0) {
			findSanta();
			moveSanta();
			addScore();
			decrease();
		}
		
		for (int i = 1; i <= P; i++) {
			System.out.print(score[i] + " ");
		}
	}
	
	// 가장 가까운 산타 찾음
	private static void findSanta() {
		ArrayList<Dist> list = new ArrayList<>();
		
		for (int i = 1; i <= P; i++) {
			if (dead[i]) {
				continue;
			}
			
			int d = (int) (Math.pow(rx - santa[i].x, 2) + Math.pow(ry - santa[i].y, 2));
			list.add(new Dist(santa[i].x, santa[i].y, d, i));
		}
		
		if (list.size() > 0) {
			Collections.sort(list);
			
			Dist min = list.get(0);
			moveRudolph(min.x, min.y, min.n);
		}
	}
	
	// 가장 가까운 산타를 향해 돌진
	private static void moveRudolph(int x, int y, int n) {
		// 산타를 향해 8방향 중 가장 가까워지는 방향으로 한 칸 돌진
		int moveX = 0;
		int moveY = 0;
		
		if (rx < x) {
			moveX = 1;
		} else if (rx > x) {
			moveX = -1;
		}
		
		if (ry < y) {
			moveY = 1;
		} else if (ry > y) {
			moveY = -1;
		}
		
		map[rx][ry] = 0; // 루돌프가 있던 칸을 빈 칸으로 만듦
		
		rx += moveX;
		ry += moveY;
		
		// 산타와 충돌한 경우
		if (rx == x && ry == y) {
			stun[n] = 2;
			score[n] += C; // 점수 증가
			
			int firstX = x + moveX * C;
			int firstY = y + moveY * C;
			
			int lastX = firstX;
			int lastY = firstY;
			
			// 상호작용
			while (isRange(lastX, lastY) && map[lastX][lastY] > 0) {
				lastX += moveX;
				lastY += moveY;
			}
			
			while (!(lastX == firstX && lastY == firstY)) {
				int prevX = lastX - moveX;
				int prevY = lastY - moveY;
				
				if (!isRange(prevX, prevY)) {
					break;
				}
				
				int id = map[prevX][prevY];
				
				if (isRange(lastX, lastY)) {
					map[lastX][lastY] = id;
					santa[id] = new Pair(lastX, lastY);
				} else {
					dead[id] = true;
				}
				
				lastX = prevX;
				lastY = prevY;
			}
			
			// 충돌한 산타 위치 갱신
			if (isRange(firstX, firstY)) {
				map[firstX][firstY] = n;
				santa[n] = new Pair(firstX, firstY);
			} else {
				dead[n] = true;
			}
		}
		
		map[rx][ry] = -1;
	}
	
	// 산타 이동
	private static void moveSanta() {
		for (int i = 1; i <= P; i++) {
			if (dead[i] || stun[i] > 0) {
				continue;
			}
			
			// 루돌프에게 가장 가까워지는 방향 구하기
			int minD = (int) (Math.pow(rx - santa[i].x, 2) + Math.pow(ry - santa[i].y, 2));
			int nd = -1;
			
			for (int d = 0; d < 4; d++) {
				int nx = santa[i].x + dx[d];
				int ny = santa[i].y + dy[d];
				
				if (!isRange(nx, ny) || map[nx][ny] > 0) {
					continue;
				}
				
				int dist = (int) (Math.pow(rx - nx, 2) + Math.pow(ry - ny, 2));
				
				if (dist < minD) {
					minD = dist;
					nd = d;
				}
			}
			
			if (nd != -1) {
				int nx = santa[i].x + dx[nd];
				int ny = santa[i].y + dy[nd];
				
				if (nx == rx && ny == ry) {
					stun[i] = 2;
					score[i] += D;
					
					if (D > 1) {
						// 상호작용
						int moveX = -dx[nd];
						int moveY = -dy[nd];
						
						int firstX = nx + moveX * D;
						int firstY = ny + moveY * D;
						
						int lastX = firstX;
						int lastY = firstY;
						
						// 상호작용
						while (isRange(lastX, lastY) && map[lastX][lastY] > 0) {
							lastX += moveX;
							lastY += moveY;
						}
						
						while (!(lastX == firstX && lastY == firstY)) {
							int prevX = lastX - moveX;
							int prevY = lastY - moveY;
							
							if (!isRange(prevX, prevY)) {
								break;
							}
							
							int id = map[prevX][prevY];
							
							if (isRange(lastX, lastY)) {
								map[lastX][lastY] = id;
								santa[id] = new Pair(lastX, lastY);
							} else {
								dead[id] = true;
							}
							
							lastX = prevX;
							lastY = prevY;
						}
						
						map[santa[i].x][santa[i].y] = 0;
						
						if (isRange(firstX, firstY)) {
							map[firstX][firstY] = i;
							santa[i] = new Pair(firstX, firstY);
						} else {
							dead[i] = true;
						}
					}
				} else {
					map[santa[i].x][santa[i].y] = 0;
					
					map[nx][ny] = i;
					santa[i] = new Pair(nx, ny);
				}
			}
		}
	}
	
	// 점수 추가
	private static void addScore() {
		for (int i = 1; i <= P; i++) {
			if (!dead[i]) {
				score[i]++;
			}
		}
	}
	
	// 기절 턴 감소
	private static void decrease() {
		for (int i = 1; i <= P; i++) {
			if (stun[i] > 0) {
				stun[i]--;
			}
		}
	}
	
	private static boolean isRange(int x, int y) {
		return x >= 0 && x < N && y >= 0 && y < N;
	}

}
