/**
 * 나무 타이쿤
 * 
 * @author minchae
 * @date 2024. 4. 2.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Queue;
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
	
	static int[] dx = {0, -1, -1, -1, 0, 1, 1, 1};
	static int[] dy = {1, 1, 0, -1, -1, -1, 0, 1};
	
	static int n, m;
	static int[][] map;
	
	static Queue<Pair> q = new LinkedList<>();
	static boolean[][] visited;

	public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        
        map = new int[n][n];
        
        for (int i = 0; i < n; i++) {
        	st = new StringTokenizer(br.readLine());
        	
        	for (int j = 0; j < n; j++) {
        		map[i][j] = Integer.parseInt(st.nextToken());
        	}
        }
        
        q.add(new Pair(n - 2, 0));
        q.add(new Pair(n - 1, 0));
        q.add(new Pair(n - 2, 1));
        q.add(new Pair(n - 1, 1));
        
        while (m-- > 0) {
        	st = new StringTokenizer(br.readLine());
        	
        	int d = Integer.parseInt(st.nextToken()) - 1;
        	int p = Integer.parseInt(st.nextToken());
        	
        	visited = new boolean[n][n];
        	
        	move(d, p);
        	grow();
        	buy();
        }

        int answer = 0;
        
        for (int i = 0; i < n; i++) {
        	for (int j = 0; j < n; j++) {
        		answer += map[i][j];
        	}
        }
        
        System.out.println(answer);
	}
	
	private static void move(int d, int p) {
		for (Pair cur : q) {
			int nx = (n + cur.x + dx[d] * (p % n)) % n;
			int ny = (n + cur.y + dy[d] * (p % n)) % n;
			
			cur.x = nx;
			cur.y = ny;
			
			map[nx][ny]++;
			visited[nx][ny] = true;
		}
	}
	
	private static void grow() {
		while (!q.isEmpty()) {
			Pair cur = q.poll();
			
			int cnt = 0;
			
			for (int i = 1; i < 8; i += 2) {
				int nx = cur.x + dx[i];
				int ny = cur.y + dy[i];
				
				if (isRange(nx, ny) && map[nx][ny] >= 1) {
					cnt++;
				}
			}
			
			map[cur.x][cur.y] += cnt;
		}
	}
	
	private static void buy() {
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (!visited[i][j] && map[i][j] >= 2) {
					map[i][j] -= 2;
					q.add(new Pair(i, j));
				}
			}
		}
	}
	
	private static boolean isRange(int x, int y) {
		return x >= 0 && x < n && y >= 0 && y < n;
	}

}
