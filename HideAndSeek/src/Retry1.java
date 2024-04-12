/**
 * 술래잡기
 * 
 * @author minchae
 * @date 2024. 4. 12.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Retry1 {
	
	// 상우하좌
	static int[] dx = {-1, 0, 1, 0};
	static int[] dy = {0, 1, 0, -1};
	
	static int[] dc = {1, 1, 2, 2};
	
	static int n, m, h, k;
	
	static ArrayList<Integer>[][] map; // 도망자의 방향 저장
	static ArrayList<Integer>[][] newMap; // 도망자가 동시에 이동하기 때문에 새로운 맵 사용
	
	static boolean[][] tree;
	
	static int sx, sy; // 술래 위치
	static boolean reverse; // 술래가 정방향으로 이동하는지, 역방향으로 이동하는지 저장
	
	static int[][] seekDir;
	static int[][] seekRevDir;
	
	static int answer;

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		n = Integer.parseInt(st.nextToken());
		m = Integer.parseInt(st.nextToken());
		h = Integer.parseInt(st.nextToken());
		k = Integer.parseInt(st.nextToken());
		
		map = new ArrayList[n][n];
		newMap = new ArrayList[n][n];
		tree = new boolean[n][n];
		seekDir = new int[n][n];
		seekRevDir = new int[n][n];
		
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				map[i][j] = new ArrayList<>();
			}
		}
		
		for (int i = 0; i < m; i++) {
			st = new StringTokenizer(br.readLine());
			
			int x = Integer.parseInt(st.nextToken()) - 1;
			int y = Integer.parseInt(st.nextToken()) - 1;
			int d = Integer.parseInt(st.nextToken());
			
			map[x][y].add(d);
		}
		
		for (int i = 0; i < h; i++) {
			st = new StringTokenizer(br.readLine());
			
			int x = Integer.parseInt(st.nextToken()) - 1;
			int y = Integer.parseInt(st.nextToken()) - 1;
			
			tree[x][y] = true;
		}
		
		sx = n / 2;
		sy = n / 2;
		
		initSeekDir();

		for (int t = 1; t <= k; t++) {
			moveAllHide();
			moveSeek();
			catchHide(t);
		}
		
		System.out.println(answer);
	}
	
	// 술래가 이동하는 방향 미리 저장
	private static void initSeekDir() {
		int x = sx;
		int y = sy;
		
		while (true) {
			for (int d = 0; d < 4; d++) {
				for (int cnt = 0; cnt < dc[d]; cnt++) {
					seekDir[x][y] = d;
							
					x += dx[d];
					y += dy[d];
					
					seekRevDir[x][y] = (d + 2) % 4;
					
					if (x == 0 && y == 0) {
						return;
					}
				}
			}
			
			for (int i = 0; i < 4; i++) {
				dc[i] += 2;
			}
		}
	}
	
	private static void moveAllHide() {
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				newMap[i][j] = new ArrayList<>();
			}
		}
		
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				int diff = Math.abs(sx - i) + Math.abs(sy - j);
				
				if (diff <= 3) {
					for (int d : map[i][j]) {
						moveHide(i, j, d);
					}
				} else {
					newMap[i][j].addAll(map[i][j]);
				}
			}
		}
		
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				map[i][j] = new ArrayList<>(newMap[i][j]);
			}
		}
	}
	
	private static void moveHide(int x, int y, int d) {
		int nx = x + dx[d];
		int ny = y + dy[d];
		
		// 격자를 벗어나는 경우 반대 방향으로 바꿈
		if (!isRange(nx, ny)) {
			d = (d + 2) % 4;
			
			nx = x + dx[d];
			ny = y + dy[d];
		}
		
		if (nx == sx && ny == sy) {
			newMap[x][y].add(d); // 이동하려는 칸에 술래가 있는 경우 움직이지 않음
		} else {
			newMap[nx][ny].add(d); // 술래가 없는 경우 움직임
		}
	}
	
	// 술래 이동
	private static void moveSeek() {
		int dir = getDir();
		
		sx += dx[dir];
		sy += dy[dir];
		
		if (sx == 0 && sy == 0 && !reverse) {
			reverse = true;
		}
		
		if (sx == n / 2 && sy == n / 2 && reverse) {
			reverse = false;
		}
	}
	
	private static int getDir() {
		return reverse ? seekRevDir[sx][sy] : seekDir[sx][sy];
	}
	
	// 도망자 잡기
	private static void catchHide(int t) {
		int dir = getDir();
		
		int nx = sx;
		int ny = sy;
		
		for (int i = 0; i < 3; i++) {
			// 격자를 벗어나지 않고 나무가 없는 경우
			if (isRange(nx, ny) && !tree[nx][ny]) {
				answer += t * map[nx][ny].size();
				map[nx][ny].clear();
			}
			
			nx += dx[dir];
			ny += dy[dir];
		}
	}
	
	private static boolean isRange(int x, int y) {
		return x >= 0 && x < n && y >= 0 && y < n;
	}

}
