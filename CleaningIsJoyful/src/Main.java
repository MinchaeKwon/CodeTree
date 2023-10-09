/**
 * 청소는 즐거워
 * 삼성 SW 역량테스트 2020 하반기 오후 1번 문제
 * https://www.codetree.ai/training-field/frequent-problems/problems/cleaning-is-joyful/description?page=2&pageSize=20
 * 
 * @author minchae
 * @date 2023. 10. 9.
 * 
 * 백준 20057 마법사 상어와 토네이도
 * https://www.acmicpc.net/problem/20057
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {
	
	// 좌하우상
	static int[] dx = {0, 1, 0, -1};
	static int[] dy = {-1, 0, 1, 0};
	
	static int[] ratio = {1, 1, 2, 7, 7, 2, 10, 10, 5}; // 먼지 퍼지는 비율
	
	// 먼지가 퍼지는 x방향
    static int[][] ddx = {
        {-1, 1, -2, -1, 1, 2, -1, 1, 0}, // 좌
        {-1, -1, 0, 0, 0, 0, 1, 1, 2}, // 하
        {-1, 1, -2, -1, 1, 2, -1, 1, 0}, // 우
        {1, 1, 0, 0, 0, 0, -1, -1, -2}  // 상
    };

    // 먼지가 퍼지는 y방향
    static int[][] ddy = {
        {1, 1, 0, 0, 0, 0, -1, -1, -2}, // 좌
        {-1, 1, -2, -1, 1, 2, -1, 1, 0}, // 하
        {-1, -1, 0, 0, 0, 0, 1, 1, 2}, // 우
        {1, -1, 2, 1, -1, -2, 1, -1, 0}  // 상
    };
	
	static int[] dc = {1, 1, 2, 2}; // 먼지가 좌하우상으로 이동할 때 각 방향으로 몇 칸씩 이동하는 칸의 수
	
	static int n;
	static int[][] map;

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		n = Integer.parseInt(br.readLine());
		
		map = new int[n][n];
		
		for (int i = 0; i < n; i++) {
			StringTokenizer st = new StringTokenizer(br.readLine());
			
			for (int j = 0; j < n; j++) {
				map[i][j] = Integer.parseInt(st.nextToken());
			}
		}
		
		int result = moveBroom(n / 2, n / 2); // 빗자루는 정중앙에서부터 움직임
		
		// 격자 바깥으로 나간 먼지의 양 출력
		System.out.println(result);
	}
	
	// 빗자루 이동시키기
	private static int moveBroom(int x, int y) {
		int dust = 0; // 격자 바깥으로 나간 먼지의 양
		
		while (true) {
			// 좌하우상으로 이동
			for (int i = 0; i < 4; i++) {
				// 빗자루는 한 번에 한 칸식 이동하기 때문에 for문 사용
				for (int cnt = 0; cnt < dc[i]; cnt++) {
					// 빗자루가 좌측 최상단에 도달한 경우 먼지의 양 반환
					if (x == 0 && y == 0) {
						return dust;
					}
					
					// 빗자루가 이동하는 방향
					int nx = x + dx[i];
					int ny = y + dy[i];
					
					int total = map[nx][ny];
					int sum = 0; // 9방향으로 퍼뜨린 먼지의 양
					
					map[nx][ny] = 0;
					
					// 9개의 방향으로 먼지를 퍼뜨림
					for (int j = 0; j < 9; j++) {
						int dustX = nx + ddx[i][j];
						int dustY = ny + ddy[i][j];
						
						int spread = (total * ratio[j]) / 100; // 퍼지는 먼지의 양
						
						if (isRange(dustX, dustY)) {
							map[dustX][dustY] += spread; // 범위를 벗어나지 않으면 해당 칸에 먼지 추가
						} else {
							dust += spread; // 범위 벗어나면 격자 벗어나는 먼지 양에 추가
						}
						
						sum += spread;
					}
					
					// 9개의 방향에 모래 퍼뜨린 후에 a자리에 먼지 추가
					
					// 빗자루 이동한 칸의 다음 캄
					int ax = nx + dx[i];
					int ay = ny + dy[i];
					
					int aSand = total - sum; // 9개의 방향에 퍼뜨리고 남은 먼지의 양
					
					if (isRange(ax, ay)) {
						map[ax][ay] += aSand; // 범위를 벗어나지 않으면 해당 칸에 먼지 추가
					} else {
						dust += aSand; // 범위 벗어나면 격자 벗어나는 먼지 양에 추가
					}
					
					x = nx;
					y = ny;
				}
			}
			
			// 빗자루가 좌하우상으로 이동하는 칸의 개수 늘림 (2개씩 늘어남)
			for (int i = 0; i < 4; i++) {
				dc[i] += 2;
			}
		}
	}
	
	private static boolean isRange(int x, int y) {
		return x >= 0 && x < n && y >= 0 && y < n;
	}

}
