/**
 * 메이즈 러너
 * 
 * @author minchae
 * @date 2023. 12. 10.
 */

import java.io.*;
import java.util.*;

public class MazeRunner {
	
	static class Pair {
		int x;
		int y;
		
		public Pair(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}

	static int N, M, K;
	
	static int[][] map;
	static Pair[] players;
	
	static int ex, ey; // 출구 좌표
	
	static int answer;
	
	static int sx, sy, size; // 정사각형의 시작 좌표와 크기
	static int[][] newMap; // 회전할 때 편리하게 하기 위해 사용하는 것
	
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());

		N = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());
		K = Integer.parseInt(st.nextToken());
		
		map = new int[N + 1][N + 1];
		newMap = new int[N + 1][N + 1];
		players = new Pair[M + 1];
		
		for (int i = 1; i <= N; i++) {
			st = new StringTokenizer(br.readLine());
			
			for (int j = 1; j <= N; j++) {
				map[i][j] = Integer.parseInt(st.nextToken());
			}
		}
		
		for (int i = 1; i <= M; i++) {
			st = new StringTokenizer(br.readLine());
			
			int x = Integer.parseInt(st.nextToken());
			int y = Integer.parseInt(st.nextToken());
			
			players[i] = new Pair(x, y);
		}
		
		st = new StringTokenizer(br.readLine());
		
		ex = Integer.parseInt(st.nextToken());
		ey = Integer.parseInt(st.nextToken());
		
		// K초동안 진행
		while (K-- > 0) {
			movePlayers();
			
			if (isEnd()) {
				break;
			}
			
			findSquare();
			
			rotateMap();
			rotatePlayers();
			rotateExit();
		}
		
		System.out.println(answer); // 모든 참가자들의 이동거리 합
		System.out.println(ex + " " + ey); // 출구 좌표
		
	}
	
	// 모든 참가자가 1칸씩 움직임
	private static void movePlayers() {
		for (int i = 1; i <= M; i++) {
			Pair cur = players[i];
			
			// 참가자가 탈출한 경우 다음으로 넘어감
			if (cur.x == ex && cur.y == ey) {
				continue;
			}
			
			// 움직일 수 있는 칸이 2개 이상이라면 상하를 우선시 하기 때문에 상하로 먼저 움직여봄
			
			// 상하로 차이가 날 경우
			if (cur.x != ex) {
				int nx = cur.x;
				int ny = cur.y;
				
				if (nx < ex) {
					nx++;
				} else {
					nx--;
				}
				
				// 내구도가 0인 빈칸인 경우 참가가 위치 옮기고 다음 참가자로 넘어감
				if (map[nx][ny] == 0) {
					cur.x = nx;
					cur.y = ny;
					
					answer++;
					
					continue;
				}
			}
			
			// 좌우로 차이가 날 경우
			if (cur.y != ey) {
				int nx = cur.x;
				int ny = cur.y;

				if (ny < ey) {
					ny++;
				} else {
					ny--;
				}

				// 내구도가 0인 빈칸인 경우 참가가 위치 옮기고 다음 참가자로 넘어감
				if (map[nx][ny] == 0) {
					cur.x = nx;
					cur.y = ny;
					
					answer++;

					continue;
				}
			}
			
		}
	}
	
	// 모든 참가자들이 미로에서 탈출했는지 확인
	private static boolean isEnd() {
		for (int i = 1; i <= M; i++) {
			// 한 명이라도 탈출하지 못한 경우 false 반환
			if (!(players[i].x == ex && players[i].y == ey)) {
				return false;
			}
		}
		
		return true;
	}
	
	// 한 명 이상의 참가자와 출구를 포함한 가장 작은 정사각형을 찾음
	private static void findSquare() {
		for (int sz = 2; sz <= N; sz++) {
			for (int x1 = 1; x1 <= N - sz + 1; x1++) {
				for (int y1 = 1; y1 <= N - sz + 1; y1++) {
					int x2 = x1 + sz - 1;
					int y2 = y1 + sz - 1;
					
					// 출구를 포함하고 있는지 확인
					if (!(ex >= x1 && ex <= x2 && ey >= y1 && ey <= y2)) {
						continue; // 포함되어 있지 않다면 다음 탐색
					}
					
					// 참가자를 포함하고 있는지 확인
					boolean isIn = false;
					
					for (int i = 1; i <= M; i++) {
						Pair cur = players[i];
						
						if (cur.x >= x1 && cur.x <= x2 && cur.y >= y1 && cur.y <= y2) {
							// 참가자가 탈출하지 않은 경우에만 true로 바꿔줌
							if (!(cur.x == ex && cur.y == ey)) {
								isIn = true;
							}
						}
					}
					
					if (isIn) {
						sx = x1;
						sy = y1;
						size = sz;
						
						return;
					}
					
				}
			}
		}
	}
	
	// 맵의 내구도 시계 방향으로 90도 회전시키기
	private static void rotateMap() {
		// 미리 내구도를 감소시켜야 회전할 때 올바른 답을 구할 수 있음
		for (int i = sx; i < sx + size; i++) {
			for (int j = sy; j < sy + size; j++) {
				if (map[i][j] > 0) {
					map[i][j]--;
				}
			}
		}
		
		for (int i = sx; i < sx + size; i++) {
			for (int j = sy; j < sy + size; j++) {
				int ox = i - sx;
				int oy = j - sy;
				
				int rx = oy;
				int ry = size - ox - 1;
				
				newMap[rx + sx][ry + sy] = map[i][j];
			}
		}
		
		// 회전한 부분 원본 맵에 반영
		for (int i = sx; i < sx + size; i++) {
			for (int j = sy; j < sy + size; j++) {
				map[i][j] = newMap[i][j];
			}
		}
		
	}
	
	// 참가자들 시계 방향으로 90도 회전시키기
	private static void rotatePlayers() {
		for (int i = 1; i <= M; i++) {
			Pair cur = players[i];
			
			// 참가자가 정사각형 안에 있는 경우에만 회전시킴
			if (cur.x >= sx && cur.x < sx + size && cur.y >= sy && cur.y < sy + size) {
				int ox = cur.x - sx;
				int oy = cur.y - sy;
				
				int rx = oy;
				int ry = size - ox - 1;
				
				cur.x = rx + sx;
				cur.y = ry + sy;
			}
		}
	}
	
	// 출구 시계 방향으로 90도 회전시키기
	private static void rotateExit() {
		// 출구가 정사각형 안에 있을 경우에 회전시킴
		if (ex >= sx && ex < sx + size && ey >= sy && ey < sy + size) {
			int ox = ex - sx;
			int oy = ey - sy;
			
			int rx = oy;
			int ry = size - ox - 1;
			
			ex = rx + sx;
			ey = ry + sy;
		}
	}

}
