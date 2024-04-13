/**
 * 메이즈 러너
 * 
 * @author minchae
 * @date 2024. 4. 13.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Retry2 {
	
	static class Pair {
		int x;
		int y;
		
		public Pair(int x, int y) {
			this.x = x;
			this.y = y;
		}
		
		public boolean isSame(Pair o) {
			return this.x == o.x && this.y == o.y;
		}
	}
	
	// 상하좌우
	static int[] dx = {-1, 1, 0, 0};
	static int[] dy = {0, 0, -1, 1};
	
	static int N, M, K;
	
	static int[][] map; // 내구도 저장
	static Pair[] person;
	static Pair end;
	
	static int sx, sy, size;
	static int[][] newMap;
	
	static int answer;

	public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        
        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        K = Integer.parseInt(st.nextToken());
        
        map = new int[N + 1][N + 1];
        person = new Pair[M + 1];
        
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
        	
        	person[i] = new Pair(x, y);
        }
        
        st = new StringTokenizer(br.readLine());
        
        int x = Integer.parseInt(st.nextToken());
        int y = Integer.parseInt(st.nextToken());
        
        end = new Pair(x, y);

        while (K-- > 0) {
        	move();
        	
        	if (isEnd()) {
        		break;
        	}
        	
        	findSquare();
        	
        	rotateMap();
        	rotatePerson();
        	rotateEnd();
        }
        
        System.out.println(answer);
        System.out.println(end.x + " " + end.y);
	}
	
	private static void move() {
		for (int i = 1; i <= M; i++) {
			Pair cur = person[i];
			
			// 참가자가 탈출한 경우 다음 참가자로 넘어감
			if (cur.isSame(end)) {
				continue;
			}
			
			int dir = -1;
			int min = Math.abs(end.x - cur.x) + Math.abs(end.y - cur.y);
			
			for (int d = 0; d < 4; d++) {
				int nx = cur.x + dx[d];
				int ny = cur.y + dy[d];
				
				if (!isRange(nx, ny) || map[nx][ny] > 0) {
					continue;
				}
				
				int dist = Math.abs(nx - end.x) + Math.abs(ny - end.y);
				
				if (dist < min) {
					min = dist;
					dir = d;
				}
			}
			
			if (dir != -1) {
				cur.x += dx[dir];
				cur.y += dy[dir];
				
				answer++;
			}
		}
	}
	
	private static void findSquare() {
		for (int sz = 2; sz <= N; sz++) {
			for (int x1 = 1; x1 <= N - sz + 1; x1++) {
				for (int y1 = 1; y1 <= N - sz + 1; y1++) {
					int x2 = x1 + sz - 1;
					int y2 = y1 + sz - 1;
					
					// 출구가 범위 안에 있는지 확인
					if (!(end.x >= x1 && end.x <= x2 && end.y >= y1 && end.y <= y2)) {
						continue;
					}
					
					boolean isIn = false;
					
					for (int i = 1; i <= M; i++) {
						Pair cur = person[i];
						
						if (cur.x >= x1 && cur.x <= x2 && cur.y >= y1 && cur.y <= y2) {
							if (!cur.isSame(end)) {
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
	
	private static void rotateMap() {
		for (int i = sx; i < sx + size; i++) {
			for (int j = sy; j < sy + size; j++) {
				if (map[i][j] > 0) {
					map[i][j]--;
				}
			}
		}
		
		newMap = new int[N + 1][N + 1];
		
		for (int i = sx; i < sx + size; i++) {
			for (int j = sy; j < sy + size; j++) {
				int ox = i - sx;
				int oy = j - sy;
				
				int rx = oy;
				int ry = size - ox - 1;
				
				newMap[rx + sx][ry + sy] = map[i][j];
			}
		}
		
		for (int i = sx; i < sx + size; i++) {
			for (int j = sy; j < sy + size; j++) {
				map[i][j] = newMap[i][j];
			}
		}
	}
	
	private static void rotatePerson() {
		for (int i = 1; i <= M; i++) {
			Pair cur = person[i];
			
			if (!(cur.x >= sx && cur.x < sx + size && cur.y >= sy && cur.y < sy + size)) {
				continue;
			}
			
			int ox = cur.x - sx;
			int oy = cur.y - sy;
			
			int rx = oy;
			int ry = size - ox - 1;
			
			cur.x = rx + sx;
			cur.y = ry + sy;
		}
	}
	
	private static void rotateEnd() {
		int ox = end.x - sx;
		int oy = end.y - sy;
		
		int rx = oy;
		int ry = size - ox - 1;
		
		end.x = rx + sx;
		end.y = ry + sy;
	}
	
	private static boolean isEnd() {
		for (int i = 1; i <= M; i++) {
			if (!person[i].isSame(end)) {
				return false;
			}
		}
		
		return true;
	}
	
	private static boolean isRange(int x, int y) {
		return x > 0 && x <= N && y > 0 && y <= N;
	}

}
