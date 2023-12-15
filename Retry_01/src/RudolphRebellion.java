/**
 * 루돌프의 반란
 * 
 * @author minchae
 * @date 2023. 12. 15.
 */

import java.io.*;
import java.util.*;

public class RudolphRebellion {
	
	static class Pair {
		int x;
		int y;
		
		public Pair(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}
	
	static class Distance implements Comparable<Distance> {
		int d;
		int x;
		int y;
		int num;
		
		public Distance(int d, int x, int y, int num) {
			this.d = d;
			this.x = x;
			this.y = y;
			this.num = num;
		}
		
		public Distance(int d, int x, int y) {
			this.d = d;
			this.x = x;
			this.y = y;
		}

		@Override
		public int compareTo(RudolphRebellion.Distance o) {
			if (this.d != o.d) {
				return this.d - o.d;
			}
			
			if (this.x != o.x) {
				return o.x - this.x;
			}
			
			return o.y - this.y;
		}
	}
	
	// 상우하좌
	static int[] dx = {-1, 0, 1, 0};
	static int[] dy = {0, 1, 0, -1};
	
	static int N, M, P, C, D;
	
	static int rx, ry; // 루돌프 위치
	static Pair[] santa;
	
	static int[][] map; // 루돌프 -1, 산타의 번호로 위치 저장
	
	static int[] stun; // 산타가 기절했는지 확인
	static boolean[] dead; // 격자 밖으로 튕겨나갔는지 확인
	
	static int[] score;

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());

		N = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());
		P = Integer.parseInt(st.nextToken()); // P명의 산타
		C = Integer.parseInt(st.nextToken()); // 루돌프와 충돌했을 때 밀려나는 칸, 얻는 점수
		D = Integer.parseInt(st.nextToken()); // 산타와 충돌했을 때 밀려나는 칸, 얻는 점수
		
		map = new int[N][N];
		santa = new Pair[P + 1];
		stun = new int[P + 1];
		dead = new boolean[P + 1];
		score = new int[P + 1];
		
		st = new StringTokenizer(br.readLine());
		
		rx = Integer.parseInt(st.nextToken()) - 1;
		ry = Integer.parseInt(st.nextToken()) - 1;
		
		map[rx][ry] = -1;
		
		for (int i = 0; i < P; i++) {
			st = new StringTokenizer(br.readLine());
			
			int n = Integer.parseInt(st.nextToken());
			int x = Integer.parseInt(st.nextToken()) - 1;
			int y = Integer.parseInt(st.nextToken()) - 1;
			
			santa[n] = new Pair(x, y);
			map[x][y] = n;
		}
		
		while (M-- > 0) {
//			decreaseTurn();
			
			ArrayList<Distance> list = new ArrayList<>();
			
			// 1. 루돌프와 가장 가까이에 있는 산타 찾음
			for (int i = 1; i <= P; i++) {
				// 격자 밖으로 벗어난 산타는 고려하지 않음
				if (dead[i]) {
					continue;
				}
				
				list.add(new Distance((int) (Math.pow(santa[i].x - rx, 2) + Math.pow(santa[i].y - ry, 2)), santa[i].x, santa[i].y, i));
			}
			
			// 산타가 존재할 경우에만 루돌프가 움직임
			if (list.size() > 0) {
				Collections.sort(list);
				Distance min = list.get(0);
				
				int minX = min.x;
				int minY = min.y;
				int minId = min.num;
				
				// 2. 루돌프가 가장 가까운 산타에게 돌진
				moveRudolph(minX, minY, minId);
			}
			
			// 3. 산타 움직임
			moveSanta();
			
			// 4. 살아있는 산타의 점수 증가
			getScore();
			
			decreaseTurn();
		}
		
		// 각 산타가 얻은 점수 출력
		for (int i = 1; i <= P; i++) {
			System.out.print(score[i] + " ");
		}
		
	}
	
	private static void moveRudolph(int x, int y, int id) {
		int moveX = 0;
		
		if (rx > x) {
			moveX = -1;
		} else if (rx < x) {
			moveX = 1;
		}
		
		int moveY = 0;
		
		if (ry > y) {
			moveY = -1;
		} else if (ry < y) {
			moveY = 1;
		}
		
		map[rx][ry] = 0; // 기존 루돌프 위치는 빈칸으로 만들어줌
		
		rx += moveX;
		ry += moveY;
		
		// 루돌프가 움직인 위치에 산타가 있어서 충돌한 경우
		if (rx == x && ry == y) {
			stun[id] = 2;
			
			int firstX = x + moveX * C;
			int firstY = y + moveY * C;
			
			int lastX = firstX;
			int lastY = firstY;
			
			// 이동한 칸에 다른 산타가 있어서 연쇄적으로 충돌이 일어나는 경우 한 칸씩 밀려남
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
				
				int num = map[prevX][prevY];
				
				if (isRange(lastX, lastY)) {
					map[lastX][lastY] = num;
					santa[num] = new Pair(lastX, lastY);
				} else {
					dead[num] = true;
				}
				
				lastX = prevX;
				lastY = prevY;
			}
			
			score[id] += C;
			
			if (isRange(firstX, firstY)) {
				map[firstX][firstY] = id;
				santa[id] = new Pair(firstX, firstY);
			} else {
				dead[id] = true;
			}
			
		}
		
		map[rx][ry] = -1;
	}
	
	// 산타 움직이기
	private static void moveSanta() {
		for (int i = 1; i <= P; i++) {
			// 격자 밖으로 벗어났거나 기절한 경우 다음으로 넘어감
			if (dead[i] || stun[i] > 0) {
				continue;
			}
			
			int minD = (int) (Math.pow(santa[i].x - rx, 2) + Math.pow(santa[i].y - ry, 2));
			int nd = -1;
			
			for (int d = 0; d < 4; d++) {
				int nx = santa[i].x + dx[d];
				int ny = santa[i].y + dy[d];
				
				if (!isRange(nx, ny) || map[nx][ny] > 0) {
					continue;
				}
				
				int dist = (int) (Math.pow(nx - rx, 2) + Math.pow(ny - ry, 2));
				
				if (dist < minD) {
					minD = dist;
					nd = d;
				}
			}
			
			if (nd != -1) {
				int nx = santa[i].x + dx[nd];
				int ny = santa[i].y + dy[nd];
				
				// 산타가 이동했는데 루돌프와 충돌한 경우
				if (nx == rx && ny == ry) {
					stun[i] = 2;
					
					// 루돌프가 이동한 방향의 반대 방향으로 튕겨나가기 때문에 - 붙여줌
					int moveX = -dx[nd];
					int moveY = -dy[nd];
					
					int firstX = nx + moveX * D;
					int firstY = ny + moveY * D;
					
					int lastX = firstX;
					int lastY = firstY;
					
					if (D == 1) {
						score[i] += D;
					} else {
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
							
							int num = map[prevX][prevY];
							
							if (isRange(lastX, lastY)) {
								map[lastX][lastY] = num;
								santa[num] = new Pair(lastX, lastY);
							} else {
								dead[num] = true;
							}
							
							lastX = prevX;
							lastY = prevY;
						}
						
						score[i] += D;
						
						map[santa[i].x][santa[i].y] = 0; // 원래 산타가 있던 위치는 빈칸으로 만듦
						
						if (isRange(firstX, firstY)) {
							 map[firstX][firstY] = i;
							 santa[i] = new Pair(firstX, firstY);
						} else {
							dead[i] = true;
						}
					}
					
				} else { // 충돌하지 않은 경우
					map[santa[i].x][santa[i].y] = 0; // 원래 산타가 있던 위치는 빈칸으로 만듦
					
					// 산타 위치 갱신
					santa[i] = new Pair(nx, ny);
					map[nx][ny] = i;
				}
				
			}
		}
	}
	
	// 턴 종료 후 살아있는 산타는 1점 증가
	private static void getScore() {
		for (int i = 1; i <= P; i++) {
			if (!dead[i]) {
				score[i]++;
			}
		}
	}
	
	// 기절한 산타의 턴 수 감소
	private static void decreaseTurn() {
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
