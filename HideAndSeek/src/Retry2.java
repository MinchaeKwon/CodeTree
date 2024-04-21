/**
 * 술래잡기
 * 
 * @author minchae
 * @date 2024. 4. 22.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

public class Retry2 {
	
	static class Node {
		int x;
		int y;
		int d;
		
		public Node(int x, int y, int d) {
			this.x = x;
			this.y = y;
			this.d = d;
		}
	}
	
	// 상우하좌
	static int[] dx = {-1, 0, 1, 0};
	static int[] dy = {0, 1, 0, -1};
	static int[] dc = {1, 1, 2, 2};
	
	static int n, m, h, k;
	
	static ArrayList<Integer>[][] map;
	static ArrayList<Integer>[][] newMap;
	static boolean[][] tree;
	
	static int[][] seekDir;
	static int[][] seekRevDir;
	static boolean reverse;
	
	static int sx, sy;
	
	static int answer;

	public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        h = Integer.parseInt(st.nextToken());
        k = Integer.parseInt(st.nextToken());
        
        map = new ArrayList[n][n];
        
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
        
        tree = new boolean[n][n];
        
        for (int i = 0; i < h; i++) {
        	st = new StringTokenizer(br.readLine());
        	
        	int x = Integer.parseInt(st.nextToken()) - 1;
        	int y = Integer.parseInt(st.nextToken()) - 1;
        	
        	tree[x][y] = true;
        }

        sx = n / 2;
        sy = n / 2;
        
        initDir();
        
        for (int t = 1; t<= k; t++) {
        	moveAllHide();
        	moveSeek();
        	catchHide(t);
        }
        
        System.out.println(answer);
	}
	
	private static void initDir() {
		seekDir = new int[n][n];
		seekRevDir = new int[n][n];
		
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
		newMap = new ArrayList[n][n];
		
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				newMap[i][j] = new ArrayList<>();
			}
		}
		
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				int dist = Math.abs(sx - i) + Math.abs(sy - j);
				
				if (dist <= 3) {
					for (int dir : map[i][j]) {
						moveHide(i, j, dir);
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
	
	private static void moveHide(int x, int y, int dir) {
		int nx = x + dx[dir];
		int ny = y + dy[dir];
		
		if (!isRange(nx, ny)) {
			dir = (dir + 2) % 4;
			nx = x + dx[dir];
			ny = y + dy[dir];
		}
		
		if (nx == sx && ny == sy) {
			newMap[x][y].add(dir);
		} else {
			newMap[nx][ny].add(dir);
		}
	}
	
	private static int getSeekDir() {
		return reverse ? seekRevDir[sx][sy] : seekDir[sx][sy];
	}
	
	private static void moveSeek() {
		int dir = getSeekDir();
		
		sx += dx[dir];
		sy += dy[dir];
		
		if (sx == 0 && sy == 0 && !reverse) {
			reverse = true;
		}
		
		if (sx == n / 2 && sy == n / 2 && reverse) {
			reverse = false;
		}
	}
	
	private static void catchHide(int t) {
		int dir = getSeekDir();
		
		int nx = sx;
		int ny = sy;
		
		for (int cnt = 0; cnt < 3; cnt++) {
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
