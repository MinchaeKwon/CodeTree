/**
 * 왕실의 기사 대결
 * 
 * @author minchae
 * @date 2024. 3. 19.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

public class Retry1 {
	
	static class Knight {
		int x;
		int y;
		int h;
		int w;
		int k;
		int dmg;
		int nx;
		int ny;
		
		public Knight(int x, int y, int h, int w, int k) {
			this.x = x;
			this.y = y;
			this.h = h;
			this.w = w;
			this.k = k;
		}
	}
	
	static int[] dx = {-1, 0, 1, 0};
	static int[] dy = {0, 1, 0, -1};
	
	static int L, N, Q;
	static int[][] map;
	static Knight[] knight;
	
	static int[] answer;

	public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        
        L = Integer.parseInt(st.nextToken());
        N = Integer.parseInt(st.nextToken());
        Q = Integer.parseInt(st.nextToken());
        
        map = new int[L + 1][L + 1];
        knight = new Knight[N + 1];
        answer = new int[N + 1];
        
        for (int i = 1; i <= L; i++) {
        	st = new StringTokenizer(br.readLine());
        	
        	for (int j = 1; j <=L; j++) {
        		map[i][j] = Integer.parseInt(st.nextToken());
        	}
        }
        
        for (int i = 1; i <= N; i++) {
        	st = new StringTokenizer(br.readLine());
        	
        	int x = Integer.parseInt(st.nextToken());
        	int y = Integer.parseInt(st.nextToken());
        	int h = Integer.parseInt(st.nextToken());
        	int w = Integer.parseInt(st.nextToken());
        	int k = Integer.parseInt(st.nextToken());
        	
        	knight[i] = new Knight(x, y, h, w, k);
        }
        
        while (Q-- > 0) {
        	st = new StringTokenizer(br.readLine());
        	
        	int id = Integer.parseInt(st.nextToken());
        	int d = Integer.parseInt(st.nextToken());
        	
        	if (knight[id].k <= 0) {
    			continue;
    		}
        	
        	for (int i = 1; i <= N; i++) {
        		Knight cur = knight[i];
        		
        		cur.nx = cur.x;
        		cur.ny = cur.y;
        		cur.dmg = 0;
        	}
        	
        	if (move(id, d)) {
        		for (int i = 1; i <= N; i++) {
        			Knight cur = knight[i];
        			
        			cur.x = cur.nx;
        			cur.y = cur.ny;
        			cur.k -= i == id ? 0 : cur.dmg;
        			
        			answer[i] += i == id ? 0 : cur.dmg;
        		}
        	}
        }

        int result = 0;
        
        for (int i = 1; i <= N; i++) {
        	if (knight[i].k > 0) {
        		result += answer[i];
        	}
        }
        
        System.out.println(result);
	}
	
	private static boolean move(int id, int d) {
		Queue<Knight> q = new LinkedList<>();
		boolean[] visited = new boolean[N + 1];
		
		q.add(knight[id]);
		visited[id] = true;
		
		while (!q.isEmpty()) {
			Knight cur = q.poll();
			
			cur.nx += dx[d];
			cur.ny += dy[d];
			
			if (cur.nx < 1 || cur.nx + cur.h - 1 > L || cur.ny < 1 || cur.ny + cur.w - 1 > L) {
				return false;
			}
			
			for (int i = cur.nx; i <= cur.nx + cur.h - 1; i++) {
				for (int j = cur.ny; j <= cur.ny + cur.w - 1; j++) {
					if (map[i][j] == 1) {
						cur.dmg++;
					} else if (map[i][j] == 2) {
						return false;
					}
				}
			}
			
			for (int i = 1; i <= N; i++) {
				int x = knight[i].x;
				int y = knight[i].y;
				int h = knight[i].h;
				int w = knight[i].w;
				int k = knight[i].k;
				
				if (visited[i] || k <= 0) {
					continue;
				}
				
				if (x > cur.nx + cur.h - 1 || cur.nx > x + h - 1) {
					continue;
				}
				
				if (y > cur.ny + cur.w - 1 || cur.ny > y + w - 1) {
					continue;
				}
				
				q.add(knight[i]);
				visited[i] = true;
			}
		}
		
		return true;
	}

}
