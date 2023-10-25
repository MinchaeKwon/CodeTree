/**
 * 메이즈 러너
 * 삼성 SW 역량테스트 2023 상반기 오후 1번 문제
 * https://www.codetree.ai/training-field/frequent-problems/problems/maze-runner/description?page=1&pageSize=20
 * 
 * @author minchae
 * @date 2023. 10. 25.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

class Pair {
	int x;
	int y;
	
	public Pair(int x, int y) {
		this.x = x;
		this.y = y;
	}
}

public class Main {
	
	static int N, M, K;
	
	static int[][] map; // 미로 정보 (벽의 내구도 저장)
	static int[][] newMap; // 정사각형 회전시킬 때 사용
	
	static Pair[] person;
	static Pair end;
	
	static int sx, sy, size; // 가장 작은 정사각형을 찾기 위한 정수
	
	static int result; // 참가자들의 이동 거리 합

	public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        
        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        K = Integer.parseInt(st.nextToken());
        
        // x, y좌표가 1보다 크게 주어지고, 출구 좌표를 출력하기 때문에 (0, 0)부터 시작하면 출력할 때 좌표 +1을 해줘야함 -> 편하게 (1, 1)부터 시작
        map = new int[N + 1][N + 1];
        newMap = new int[N + 1][N + 1];
        person = new Pair[M + 1];
        
        for (int i = 1; i <= N; i++) {
        	st = new StringTokenizer(br.readLine());
        	
        	for (int j = 1; j <= N; j++) {
        		map[i][j] = Integer.parseInt(st.nextToken());
        	}
        }
        
        // 참가자들의 좌표
        for (int i = 1; i <= M; i++) {
        	st = new StringTokenizer(br.readLine());
        	
        	int x = Integer.parseInt(st.nextToken());
        	int y = Integer.parseInt(st.nextToken());
        	
        	person[i] = new Pair(x, y);
        }
        
        // 출구 좌표
        st = new StringTokenizer(br.readLine());
        
        int x = Integer.parseInt(st.nextToken());
        int y = Integer.parseInt(st.nextToken());
        
        end = new Pair(x, y);
        
        // K초동안 진행
        while (K-- > 0) {
        	// 참가자 이동
        	move();
        	
        	// 모든 참가자들이 미로를 탈출한 경우 종료
        	if (isEnd()) {
        		break;
        	}
        	
        	// 가장 작은 정사각형 찾기
        	findSquare();
        	
        	rotateSquare();
        	rotatePerson();
        	rotateEnd();
        	
        }
        
        System.out.println(result);
        System.out.println(end.x + " " + end.y);
	}
	
	// 모든 참가자가 한 칸씩 이동함
	private static void move() {
		for (int i = 1; i <= M; i++) {
			// 이미 탈출한 경우 다음으로 넘어감
			if (person[i].x == end.x && person[i].y == end.y) {
				continue;
			}
			
			// 움직일 수 있는 칸이 2개 이상이라면, 상하로 움직이는 것을 우선시 하기 때문에 상하로 먼저 움직여봄
			
			// 해당 참가자와 출구가 상하로 차이날 경우
			if (person[i].x != end.x) {
				int nx = person[i].x;
				int ny = person[i].y;
				
				if (nx > end.x) {
					nx--; // 출구보다 밑에 있을 경우 한 칸 위로 이동
				} else {
					nx++; // 아닌 경우 한 칸 아래로 이동
				}
				
				// 벽이 없는 곳일 경우 (빈칸인 경우, 빈칸이 아니면 제자리)
				if (map[nx][ny] == 0) {
					person[i].x = nx;
					person[i].y = ny;
					
					result++;
					
					continue; // 다음 참가자로 넘어감
				}
			}
			
			// 좌우로 움직여봄
			if (person[i].y != end.y) {
				int nx = person[i].x;
				int ny = person[i].y;
				
				if (ny > end.y) {
					ny--; // 출구보다 왼쪽에 있을 경우 한 칸 오른쪽으로 이동
				} else {
					ny++; // 아닌 경우 한 칸 왼쪽으로 이동
				}
				
				// 벽이 없는 곳일 경우 (빈칸인 경우)
				if (map[nx][ny] == 0) {
					person[i].x = nx;
					person[i].y = ny;
					
					result++;
					
					continue; // 다음 참가자로 넘어감
				}
			}
		}
	}
	
	// 한 명 이상의 참가자와 출구를 포함한 가장 작은 정사각형을 찾음
	private static void findSquare() {
		// 2칸짜리 가장 작은 정사각형부터 시작
		for (int sz = 2; sz <= N; sz++) {
			// 가장 좌상단인 x좌표가 작은 곳부터 시작
			for (int x1 = 1; x1 <= N - sz + 1; x1++) {
				// 가장 좌상단인 y좌표가 작은 곳부터 시작
				for (int y1 = 1; y1 <= N - sz + 1; y1++) {
					int x2 = x1 + sz - 1; // 시작과 끝의 차이가 (사이즈 - 1)만큼 나기 때문에 -1을 해줌
					int y2 = y1 + sz - 1;
					
					// 해당 정사각형에 출구가 포함되어 있는지 확인
					if (!(end.x >= x1 && end.x <= x2 && end.y >= y1 && end.y <= y2)) {
						continue; // 포함되어 있지 않다면 다음 탐색
					}
					
					// 한 명 이상의 참가자가 포함되어 있는지 확인
					boolean isIn = false;
					
					for (int i = 1; i <= M; i++) {
						// 참가자가 범위 안에 있는 경우
						if (person[i].x >= x1 && person[i].x <= x2 && person[i].y >= y1 && person[i].y <= y2) {
							// 참가자가 출구에 있지 않을 경우에만 true로 변경
							if (!(person[i].x == end.x && person[i].y == end.y)) {
								isIn = true;
							}
						}
					}
					
					if (isIn) {
						sx = x1;
						sy = y1;
						size = sz;
						
						return; // 가장 작은 정사각형을 찾았기 때문에 함수 종료
					}
				}
			}
		}
	}
	
	// 정사각형 회전시키기 (회전할 때, 내구도가 1씩 깎임)
	private static void rotateSquare() {
		// 회전시킬 정사각형의 내구도 1 감소
		for (int i = sx; i < sx + size; i++) {
			for (int j = sy; j < sy + size; j++) {
				if (map[i][j] > 0) {
					map[i][j]--;
				}
			}
		}
		
		// 정사각형 회전 시킴
		for (int i = sx; i < sx + size; i++) {
			for (int j = sy; j < sy + size; j++) {
				// 기준점을 (0, 0)으로 맞춤
				int ox = i - sx;
				int oy = j - sy;
				
				// 회전시킬 좌표를 구함 - 시계 방향 90도 회전 (x, y) -> (y, size - x - 1)
				int rx = oy;
				int ry = size - ox - 1;
				
				newMap[rx + sx][ry + sy] = map[i][j]; // 원래 위치로 다시 옮겨줌
			}
		}
		
		// 회전된 부분만 원본 맵에 반영
		for (int i = sx; i < sx + size; i++) {
			for (int j = sy; j < sy + size; j++) {
				map[i][j] = newMap[i][j];
			}
		}
	}
	
	// 정사각형 안의 참가자 회전시킴
	private static void rotatePerson() {
		for (int i = 1; i <= M; i++) {
			int x = person[i].x;
			int y = person[i].y;
			
			// 참가자가 정사각형 안에 있는 경우에만 회전시킴
			if (x >= sx && x < sx + size && y >= sy && y < sy + size) {
				int ox = x - sx;
				int oy = y - sy;
				
				int rx = oy;
				int ry = size - ox - 1;
				
				person[i].x = rx + sx;
				person[i].y = ry + sy;
			}
		}
	}
	
	// 출구 회전시킴
	private static void rotateEnd() {
		int x = end.x;
		int y = end.y;
		
		// 출구가 정사각형 안에 있을 경우에 회전시킴
		if (x >= sx && x < sx + size && y >= sy && y < sy + size) {
			int ox = x - sx;
			int oy = y - sy;
			
			int rx = oy;
			int ry = size - ox - 1;
			
			end.x = rx + sx;
			end.y = ry + sy;
		}
	}
	
	// 모든 참가자들이 미로를 탈출했는지 확인
	private static boolean isEnd() {
		for (int i = 1; i <= M; i++) {
			// 한 명이라도 탈출하지 못한 경우 false 반환
			if (!(person[i].x == end.x && person[i].y == end.y)) {
				return false;
			}
		}
		
		return true;
	}

}
