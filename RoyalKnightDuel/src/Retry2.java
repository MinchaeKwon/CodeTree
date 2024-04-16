/**
 * 왕실의 기사 대결
 * 
 * @author minchae
 * @date 2024. 4. 15.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

public class Retry2 {
	
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
	
	// 상우하좌
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
        	
        	for (int j = 1; j <= L; j++) {
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
        	
        	// 대미지, 다음 이동 위치 초기화
        	for (int i = 1; i <= N; i++) {
        		Knight cur = knight[i];
        		
        		cur.dmg = 0;
    			cur.nx = cur.x;
    			cur.ny = cur.y;
        	}
        	
        	if (move(id, d)) {
        		for (int i = 1; i <= N; i++) {
        			Knight cur = knight[i];
        			
        			cur.x = cur.nx;
        			cur.y = cur.ny;
        			
        			// 명령을 받은 기사는 체력이 감소하지 않음
        			cur.k -= i != id ? cur.dmg : 0;
        			answer[i] += i != id ? cur.dmg : 0;
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
		boolean[] move = new boolean[N + 1];
		
		q.add(knight[id]);
		move[id] = true;
		
		while (!q.isEmpty()) {
			Knight cur = q.poll();
			
			int nx = cur.x + dx[d];
			int ny = cur.y + dy[d];
			
			// 이동한 칸이 범위를 벗어나면 이동할 수 없음
			if (nx < 1 || nx + cur.h - 1 > L || ny < 1 || ny + cur.w - 1 > L) {
				return false;
			}
			
			int cnt = 0; // 이동한 곳에 있는 함정의 개수
			
			for (int i = nx; i < nx + cur.h; i++) {
				for (int j = ny; j < ny + cur.w; j++) {
					if (map[i][j] == 1) {
						cnt++;
					} else if (map[i][j] == 2) {
						return false;
					}
				}
			}
			
			// 이동할 위치와 대미지 저장
			cur.dmg = cnt;
			cur.nx = nx;
			cur.ny = ny;
			
			// 이동한 곳이 다른 기사와 겹치는지 확인
			for (int i = 1; i <= N; i++) {
				if (move[i] || knight[i].k <= 0) {
					continue;
				}
				
				// 이동한 x범위가 다른 기사의 x범위와 겹치거나 다른 기사의 x범위가 이동한 x범위와 겹칠 경우
				if (nx > knight[i].x + knight[i].h - 1 || knight[i].x > nx + cur.h - 1) {
					continue;
				}
				
				if (ny > knight[i].y + knight[i].w - 1 || knight[i].y > ny + cur.w - 1) {
					continue;
				}
				
				// 겹치는 기사는 연쇄적으로 움직임
				q.add(knight[i]);
				move[i] = true;
			}
		}
		
		return true;
	}

}
