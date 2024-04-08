/**
 * 청소는 즐거워
 * 
 * @author minchae
 * @date 2024. 4. 8.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Retry1 {
	
	// 좌하우상
	static int[] dx = {0, 1, 0, -1};
	static int[] dy = {-1, 0, 1, 0};
	
	// 매 턴마다 빗자루가 이동하는 칸의 개수
	static int[] dc = {1, 1, 2, 2};
	
	// 먼지가 퍼지는 비율
	static int[] ratio = {1, 1, 2, 7, 7, 2, 10, 10, 5};
	
	// 먼지가 이동하는 방향
	static int[][] ddx = {
		{-1, 1, -2, -1, 1, 2, -1, 1, 0},
		{-1, -1, 0, 0, 0, 0, 1, 1, 2},
		{-1, 1, -2, -1, 1, 2, -1, 1, 0},
		{1, 1, 0, 0, 0, 0, -1, -1, -2}
	};
	
	static int[][] ddy = {
		{1, 1, 0, 0, 0, 0, -1, -1, -2},
		{-1, 1, -2, -1, 1, 2, -1, 1, 0},
		{-1, -1, 0, 0, 0, 0, 1, 1, 2},
		{1, -1, 2, 1, -1, -2, 1, -1, 0}
	};
	
	static int n;
	static int[][] map;
	
	static int answer;

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

		move(n / 2, n / 2); // 빗자루는 중앙에서부터 움직임
		
		System.out.println(answer);
	}
	
	private static void move(int x, int y) {
		while (true) {
			// 빗자루가 좌하우상으로 움직임
			for (int d = 0; d < 4; d++) {
				for (int cnt = 0; cnt < dc[d]; cnt++) {
					// 빗자루가 이동하는 칸
					int nx = x + dx[d];
					int ny = y + dy[d];
					
					int total = map[nx][ny];
					int sum = 0; // 퍼뜨린 모래의 양
					
					map[nx][ny] = 0;
					
					// 9개의 곳으로 먼지 퍼뜨림
					for (int i = 0; i < 9; i++) {
						int dustX = nx + ddx[d][i];
						int dustY = ny + ddy[d][i];
						
						int spread = total * ratio[i] / 100;
						
						if (isRange(dustX, dustY)) {
							map[dustX][dustY] += spread;
						} else {
							answer += spread;
						}
						
						sum += spread;
					}
					
					int ax = nx + dx[d];
					int ay = ny + dy[d];
					
					int spread = total - sum;
					
					if (isRange(ax, ay)) {
						map[ax][ay] += spread;
					} else {
						answer += spread;
					}
					
					x = nx;
					y = ny;
					
					if (x == 0 && y == 0) {
						return;
					}
				}
			}
			
			// 빗자루가 이동하는 칸의 개수 늘림
			for (int i = 0; i < 4; i++) {
				dc[i] += 2;
			}
		}
	}
	
	private static boolean isRange(int x, int y) {
		return x >= 0 && x < n && y >= 0 && y < n;
	}

}
