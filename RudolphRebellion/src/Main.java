/**
 * 루돌프의 반란
 * 삼성 SW 역량테스트 2023 하반기 오후 1번 문제
 * https://www.codetree.ai/training-field/frequent-problems/problems/rudolph-rebellion/description?page=1&pageSize=20
 * 
 * @author minchae
 * @date 2023. 12. 6.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

class Santa {
	int x;
	int y;
	
	public Santa(int x, int y) {
		this.x = x;
		this.y = y;
	}
}

class Distance implements Comparable<Distance> {
	int d;
	int x;
	int y;
	
	public Distance(int d, int x, int y) {
		this.d = d;
		this.x = x;
		this.y = y;
	}

	@Override
	public int compareTo(Distance o) {
		if (this.d != o.d) {
			return this.d - o.d;
		}
		
		if (this.x != o.x) {
			return o.x - this.x;
		}
		
		return o.y - this.y;
	}
}

public class Main {
	
	// 상우하좌
	static int[] dx = {-1, 0, 1, 0};
	static int[] dy = {0, 1, 0, -1};
	
	static int N, M, P, C, D;
	static int rx, ry; // 루돌프의 위치
	
	static int[][] map; // 산타와 루돌프의 위치 저장 (산타 번호, 루돌프 -1)
	static Santa[] santaArr;
	
	static int[] stun; // 산타가 기절했는지 확인
	static boolean[] dead; // 산타가 죽었는지 확인
	
	static int[] score; // 산타가 얻은 점수 저장

	public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        
        N = Integer.parseInt(st.nextToken()); // 게임판의 크기
        M = Integer.parseInt(st.nextToken()); // 게임 턴 수
        P = Integer.parseInt(st.nextToken()); // 산타의 수
        C = Integer.parseInt(st.nextToken()); // 루돌프의 힘
        D = Integer.parseInt(st.nextToken()); // 산타의 힘
        
        map = new int[N][N];
        santaArr = new Santa[P + 1];
        stun = new int[P + 1];
        dead = new boolean[P + 1];
        score = new int[P + 1];
        
        st = new StringTokenizer(br.readLine());
        
        // 루돌프 위치 표시
        rx = Integer.parseInt(st.nextToken()) - 1;
        ry = Integer.parseInt(st.nextToken()) - 1;
        
        map[rx][ry] = -1;
        
        // 산타 위치 표시
        for (int i = 0; i < P; i++) {
        	st = new StringTokenizer(br.readLine());
        	
        	int num = Integer.parseInt(st.nextToken());
        	int x = Integer.parseInt(st.nextToken()) - 1;
        	int y = Integer.parseInt(st.nextToken()) - 1;
        	
        	santaArr[num] = new Santa(x, y);
        	
        	map[x][y] = num;
        }
        
        // 게임 턴 수만큼 반복
        while (M-- > 0) {
        	int minX = 10000;
        	int minY = 10000;
        	int minId = 0;
        	
        	// 1. 가장 가까운 산타 찾음
    		for (int i = 1; i <= P; i++) {
    			// 격자 밖을 벗어난 산타일 경우 다음으로 넘어감
    			if (dead[i]) {
    				continue;
    			}
    			
    			Distance min = new Distance((int) (Math.pow(minX - rx, 2) + Math.pow(minY - ry, 2)), minX, minY);
    			Distance cur = new Distance((int) (Math.pow(santaArr[i].x - rx, 2) + Math.pow(santaArr[i].y - ry, 2)), santaArr[i].x, santaArr[i].y);
    			
    			if (cur.compareTo(min) < 0) {
    				minX = cur.x;
    				minY = cur.y;
    				minId = i;
    			}
    		}
    		
//    		System.out.println(minX + " " + minY + " " + minId);
        	
    		// 2. 루돌프가 가장 가까운 산타를 향해 돌진
    		if (minId != 0) {
    			moveRudolph(minX, minY, minId);
    		}
        	
        	// 3. 산타가 루돌프와 가장 가까운 방향으로 움직임
        	moveSanta();
        	
        	// 4. 죽지 않은 산타의 점수 증가시킴
        	getScore();
        	
        	// 5. 기절한 산타의 턴 수를 감소시킴
        	decreaseTurn();
        }
        
        // 산타가 얻은 최종점수 출력
        for (int i = 1; i <= P; i++) {
        	System.out.print(score[i] + " ");
        }
        
	}
	
	// 루돌프와 가장 가까운 산타 위치, 번호를 매개변수로 받음
	private static void moveRudolph(int x, int y, int id) {
		int moveX = 0;
		
		if (x > rx) {
			moveX = 1;
		} else if (x < rx) {
			moveX = -1;
		}
		
		int moveY = 0;
		
		if (y > ry) {
			moveY = 1;
		} else if (y < ry) {
			moveY = -1;
		}
		
		// 루돌프 위치 기억
		int prevRx = rx;
		int prevRy = ry;
		
		// 가장 가까운 산타 쪽으로 움직임
		rx += moveX;
		ry += moveY;
		
		map[prevRx][prevRy] = 0; // 기존에 루돌프가 있던 위치는 빈칸으로 만들어줌
		
		// 루돌프가 움직여서 산타와 충돌한 경우
		if (rx == x && ry == y) {
			// 루돌프가 이동해온 방향으로 C칸 움직임
			int firstX = x + moveX * C;
			int firstY = y + moveY * C;
			
			// 충돌이 일어나서 상호작용이 일어날 수도 있기 때문에 저장해두는 것
			int lastX = firstX;
			int lastY = firstY;
			
			stun[id] = 2; // 충돌이 일어났으니까 기절시킴
			
			// 격자 범위 안이고, 다른 산타가 있는 경우 연쇄적으로 충돌이 일어남
			while (isRange(lastX, lastY) && map[lastX][lastY] > 0) {
				// 루돌프가 이동한 방향으로 계속 한 칸씩 움직임
				lastX += moveX;
				lastY += moveY;
			}
			
			// 연쇄적으로 충돌이 일어난 마지막 위치에서부터 시작해서 산타를 이동시킴
			while (!(lastX == firstX && lastY == firstY)) {
				// map[prevX][prevY]에 있던 산타가 map[lastX][lastY]로 이동하기 때문에 prevX, prevY 구해줌
				int prevX = lastX - moveX;
				int prevY = lastY - moveY;
				
				// 범위를 벗어나는 경우 종료
				if (!isRange(prevX, prevY)) {
					break;
				}
				
				int idx = map[prevX][prevY];
				
				if (isRange(lastX, lastY)) {
					// 격자 안인 경우 이전 칸에 있던 산타를 현재 칸으로 옮겨줌
					map[lastX][lastY] = idx;
					santaArr[idx] = new Santa(lastX, lastY);
				} else {
					dead[idx] = true; // 격자 밖을 벗어나는 경우 죽은 표시 해줌
				}
				
				lastX = prevX;
				lastY = prevY;
			}
			
			// 해당 산타 점수 증가시키고 위치 옮김
			score[id] += C;
			
			santaArr[id] = new Santa(firstX, firstY);
			
			if (isRange(firstX, firstY)) {
				map[firstX][firstY] = id;
			} else {
				dead[id] = true;
			}
			
		}
		
		map[rx][ry] = -1; // 루돌프 위치 옮김
	}
	
	// 산타 이동시킴
	private static void moveSanta() {
		for (int i = 1; i <= P; i++) {
			// 격자 밖을 벗어난 산타일 경우 다음으로 넘어감
			if (dead[i] || stun[i] > 0) {
				continue;
			}
			
			int minDist = (int) (Math.pow(santaArr[i].x - rx, 2) + Math.pow(santaArr[i].y - ry, 2));
			int moveDir = -1;
			
			// 방향 우선순위에 따라 상우하좌 4방향 이동해보면서 루돌프와 가장 가까운 방향 찾음
			for (int d = 0; d < 4; d++) {
				int nx = santaArr[i].x + dx[d];
				int ny = santaArr[i].y + dy[d];
				
				// 격자 밖이거나 다른 산타가 있는 경우 다음으로 넘어감
				if (!isRange(nx, ny) || map[nx][ny] > 0) {
					continue;
				}
				
				int dist = (int) (Math.pow(nx - rx, 2) + Math.pow(ny - ry, 2));
				
				if (dist < minDist) {
					minDist = dist;
					moveDir = d;
				}
			}
			
			if (moveDir != -1) {
				// 다음 이동위치 구함
				int nx = santaArr[i].x + dx[moveDir];
				int ny = santaArr[i].y + dy[moveDir];
				
				if (nx == rx && ny == ry) { // 이동한 산타가 루돌프와 충돌한 경우
					stun[i] = 2;
					
					// 충돌한 경우에는 해당 산타가 이동한 방향의 반대 방향으로 밀려나기 때문에 - 붙여줌
					int moveX = -dx[moveDir];
					int moveY = -dy[moveDir];
					
					int firstX = nx + moveX * D;
					int firstY = ny + moveY * D;
					
					int lastX = firstX;
					int lastY = firstY;
					
					if (D == 1) { // 밀려나는 칸이 한 칸인 경우
						// 루돌프와 충돌하고 자신이 이동해온 방향의 반대 방향으로 이동하면 원래 위치에 그대로 있게 됨 -> 점수만 증가시킴
						score[i] += D;
					} else {
						// 격자 안이고 다른 산타가 있는 경우
						while (isRange(lastX, lastY) && map[lastX][lastY] > 0) {
							// 한 칸씩 밀려남
							lastX += moveX;
							lastY += moveY;
						}
						
						// 연쇄적으로 충돌이 일어난 마지막 위치에서부터 시작해서 산타를 이동시킴
						while (!(lastX == firstX && lastY == firstY)) {
							// map[prevX][prevY]에 있던 산타가 map[lastX][lastY]로 이동하기 때문에 prevX, prevY 구해줌
							int prevX = lastX - moveX;
							int prevY = lastY - moveY;
							
							// 범위를 벗어나는 경우 종료
							if (!isRange(prevX, prevY)) {
								break;
							}
							
							int idx = map[prevX][prevY];
							
							if (isRange(lastX, lastY)) {
								// 격자 안인 경우 이전 칸에 있던 산타를 현재 칸으로 옮겨줌
								map[lastX][lastY] = idx;
								santaArr[idx] = new Santa(lastX, lastY);
							} else {
								dead[idx] = true; // 격자 밖을 벗어나는 경우 죽은 표시 해줌
							}
							
							lastX = prevX;
							lastY = prevY;
						}
						
						// 해당 산타 점수 증가시키고 위치 옮김
						score[i] += D;
						
						map[santaArr[i].x][santaArr[i].y] = 0;
						santaArr[i] = new Santa(firstX, firstY);
						
						if (isRange(firstX, firstY)) {
							map[firstX][firstY] = i;
						} else {
							dead[i] = true;
						}
					}
					
				} else { // 충돌하지 않은 경우
					map[santaArr[i].x][santaArr[i].y] = 0; // 원래 산타가 있던 위치는 빈칸으로 만듦
					
					// 해당 산타 위치 갱신
					santaArr[i] = new Santa(nx, ny);
					map[nx][ny] = i;
				}
				
			}
			
		}
	}
	
	// 산타 점수 증가
	private static void getScore() {
		for (int i = 1; i <= P; i++) {
			if (!dead[i]) {
				score[i]++;
			}
		}
	}
	
	// 기절한 산타의 턴 수를 감소시킴
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
