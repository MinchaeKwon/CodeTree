/**
 * 메이즈 러너
 * 
 * @author minchae
 * @date 2024. 3. 21.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Retry1 {
	
	static class Pair {
		int x;
		int y;
		
		public Pair(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}
	
	static int N, M, K;
	
	static int[][] map; // 미로 표시
	static Pair[] person;
	static Pair end;
	
	static int sx, sy, size;
	static int[][] newMap;
	
	static int moveCnt;

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

        System.out.println(moveCnt);
        System.out.println(end.x + " " + end.y);
	}
	
	private static void move() {
		for (int i = 1; i <= M; i++) {
			Pair p = person[i];
			
			if (p.x == end.x && p.y == end.y) {
				continue;
			}
			
			if (p.x != end.x) {
				int nx = p.x;
				int ny = p.y;
				
				nx += nx < end.x ? 1 : -1;
				
				if (map[nx][ny] == 0) {
					p.x = nx;
					p.y = ny;
					
					moveCnt++;
					
					continue;
				}
			}
			
			if (p.y != end.y) {
				int nx = p.x;
				int ny = p.y;
				
				ny += ny < end.y ? 1 : -1;
				
				if (map[nx][ny] == 0) {
					p.x = nx;
					p.y = ny;
					
					moveCnt++;
					
					continue;
				}
			}
		}
	}
	
	private static void findSquare() {
		for (int sz = 2; sz <= N; sz++) {
			for (int x1 = 1; x1 <= N - sz + 1; x1++) {
				for (int y1 = 1; y1 <= N - sz + 1; y1++) {
					int x2 = x1 + sz - 1;
					int y2 = y1 + sz - 1;
					
					boolean isIn = false;
					
					if (!(end.x >= x1 && end.x <= x2 && end.y >= y1 && end.y <= y2)) {
						continue;
					}
					
					for (int i = 1; i <= M; i++) {
						Pair p = person[i];
						
						if (p.x >= x1 && p.x <= x2 && p.y >= y1 && p.y <= y2) {
							if (!(p.x == end.x && p.y == end.y)) {
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
		newMap = new int[N + 1][N + 1];
		
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
		
		for (int i = sx; i < sx + size; i++) {
			for (int j = sy; j < sy + size; j++) {
				map[i][j] = newMap[i][j];
			}
		}
	}
	
	private static void rotatePerson() {
		for (int i = 1; i <= M; i++) {
			Pair p = person[i];
			
			if (p.x >= sx && p.x < sx + size && p.y >= sy && p.y < sy + size) {
				int ox = p.x - sx;
				int oy = p.y - sy;
				
				int rx = oy;
				int ry = size - ox - 1;
				
				p.x = rx + sx;
				p.y = ry + sy;
			}
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
			if (!(person[i].x == end.x && person[i].y == end.y)) {
				return false;
			}
		}
		
		return true;
	}

}
