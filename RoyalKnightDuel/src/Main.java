/**
 * 왕실의 기사 대결
 * 삼성 SW 역량테스트 2023 하반기 오전 1번 문제
 * https://www.codetree.ai/training-field/frequent-problems/problems/royal-knight-duel/description?page=1&pageSize=20
 * 
 * @author minchae
 * @date 2023. 12. 7.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

class Knight {
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

public class Main {
	
	// 상우하좌
	static int[] dx = {-1, 0, 1, 0};
	static int[] dy = {0, 1, 0, -1};
	
	static int L, N, Q;
	
	static int[][] map; // 0 빈칸, 1 함정, 2 벽
	static Knight[] knights;

	public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        L = Integer.parseInt(st.nextToken());
        N = Integer.parseInt(st.nextToken());
        Q = Integer.parseInt(st.nextToken());
        
        map = new int[L + 1][L + 1];
        knights = new Knight[N + 1];
        
        for (int i = 1; i <= L; i++) {
        	st = new StringTokenizer(br.readLine());
        	
        	for (int j = 1; j <= L; j++) {
        		map[i][j] = Integer.parseInt(st.nextToken());
        	}
        }
        
        int[] strength = new int[N + 1];
        
        for (int i = 1; i <= N; i++) {
        	st = new StringTokenizer(br.readLine());
        	
        	int r = Integer.parseInt(st.nextToken());
        	int c = Integer.parseInt(st.nextToken());
        	int h = Integer.parseInt(st.nextToken()); // 높이
        	int w = Integer.parseInt(st.nextToken()); // 너비
        	int k = Integer.parseInt(st.nextToken()); // 기사의 체력
        	
        	strength[i] = k;
        	knights[i] = new Knight(r, c, h, w, k);
        }
        
        while (Q-- > 0) {
        	st = new StringTokenizer(br.readLine());
        	
        	int n = Integer.parseInt(st.nextToken()); // 기사 번호
        	int d = Integer.parseInt(st.nextToken()); // 이동 방향
        	
        	// 체력이 0이상인 경우에만 움직임
        	if (knights[n].k > 0) {
        		// 초기화 시켜줌
        		for (int i = 1; i <= N; i++) {
        			Knight cur = knights[i];
        			
        			cur.nx = cur.x;
        			cur.ny = cur.y;
        			cur.dmg = 0;
        		}
        		
        		if (moveKnight(n, d)) {
        			for (int i = 1; i <= N; i++) {
        				Knight cur = knights[i];
        				
        				cur.x = cur.nx;
        				cur.y = cur.ny;
        				cur.k -= i == n ? 0 : cur.dmg; // 명령을 받는 기사는 데미지 영향을 받지 않음
        			}
        		}
        	}
        }
        
        // 생존한 기사들의 데미지의 합 구함
        int result = 0;
        
        for (int i = 1; i <= N; i++) {
			Knight cur = knights[i];
			
			// 생존한 기사일 경우
        	if (cur.k > 0) {
        		result += strength[i] - cur.k;
        	}
        }
        
        System.out.println(result);
	}
	
	private static boolean moveKnight(int id, int dir) {
		Queue<Knight> q = new LinkedList<>();
		boolean[] visited = new boolean[N + 1];
		
		q.add(knights[id]);
		visited[id] = true;
		
		while (!q.isEmpty()) {
			Knight cur = q.poll();
			
			// 해당 기사가 이동할 다음 위치 구함
			cur.nx += dx[dir];
			cur.ny += dy[dir];
			
			// 격자를 벗어나는 경우 움직일 수 없음
			if (cur.nx < 1 || cur.nx + cur.h - 1 > L || cur.ny < 1 || cur.ny + cur.w - 1 > L) {
				return false;
			}
			
			// 해당 기사 움직일 때 함정이나 벽을 만나는지 확인
			for (int i = cur.nx; i <= cur.nx + cur.h - 1; i++) {
				for (int j = cur.ny; j <= cur.ny + cur.w - 1; j++) {
					if (map[i][j] == 1) {
						cur.dmg++; // 함정이 있는 경우에는 데미지 증가시킴
					} else if (map[i][j] == 2) {
						return false; // 벽을 만난 경우 움직일 수 없으므로 종료
					}
				}
			}
			
			// 다른 기사와 충돌하는 경우 해당 기사 위치를 옮김
			for (int i = 1; i <= N; i++) {
				int x = knights[i].x;
				int y = knights[i].y;
				int h = knights[i].h;
				int w = knights[i].w;
				int k = knights[i].k;
				
				// 이미 이동시켰거나 체력이 없는 기사일 경우에는 다음으로 넘어감
				if (visited[i] || k <= 0) {
					continue;
				}
				
				// 충돌하지 않은 경우에는 다음으로 넘어감
				if (x > cur.nx + cur.h - 1 || cur.nx > x + h - 1) {
					continue;
				}
				
				if (y > cur.ny + cur.w - 1 || cur.ny > y + w - 1) {
					continue;
				}
				
				// 충돌하는 경우에는 이동할 다음 위치를 구하기 위해 큐에 넣어줌
				visited[i] = true;
				q.add(knights[i]);
			}
			
		}
		
		return true;
	}

}
