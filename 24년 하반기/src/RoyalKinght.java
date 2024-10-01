/**
 * 왕실의 기사 대결
 * 23 하 오전 1번
 *
 * @author minchae
 * @date 2024. 10. 1. 10:04 ~ 11: 20
 * 
 * 메모리 9MB  시간 168ms
 * */

import java.io.*;
import java.util.*;

public class RoyalKinght {
	
	static class Node {
		int x;
		int y;
		int h;
		int w;
		int k;
		int dmg;
		int nx;
		int ny;
		
		public Node(int x, int y, int h, int w, int k) {
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
	
	static int[][] map; // 0 빈칸, 1 함정, 2 벽
	static Node[] knight;
	
	static int[] result;

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		L = Integer.parseInt(st.nextToken());
		N = Integer.parseInt(st.nextToken());
		Q = Integer.parseInt(st.nextToken());
		
		map = new int[L][L];
		knight = new Node[N + 1];
		result = new int[N + 1];
		
		for (int i = 0; i < L; i++) {
			st = new StringTokenizer(br.readLine());
			
			for (int j = 0; j < L; j++) {
				map[i][j] = Integer.parseInt(st.nextToken());
			}
		}
		
		for (int i = 1; i <= N; i++) {
			st = new StringTokenizer(br.readLine());
			
			int x = Integer.parseInt(st.nextToken()) - 1;
			int y = Integer.parseInt(st.nextToken()) - 1;
			int h = Integer.parseInt(st.nextToken());
			int w = Integer.parseInt(st.nextToken());
			int k = Integer.parseInt(st.nextToken());
			
			knight[i] = new Node(x, y, h, w, k);
		}
		
		while (Q-- > 0) {
			st = new StringTokenizer(br.readLine());
			
			int id = Integer.parseInt(st.nextToken());
			int d = Integer.parseInt(st.nextToken());
			
			// 체스판에서 사라진 기사인 경우 다음 명령으로 넘어감
			if (knight[id].k <= 0) {
				continue;
			}
			
			// 대미지, 다음 위치 초기화
			for (int i = 1; i <= N; i++) {
				Node cur = knight[i];
				
				cur.dmg = 0;
				cur.nx = cur.x;
				cur.ny = cur.y;
			}
			
			// 움직일 수 있는 경우
			if (move(id, d)) {
				for (int i = 1; i <= N; i++) {
					Node cur = knight[i];
					
					// 저장된 다음 위치로 현재 위치 갱신
					cur.x = cur.nx;
					cur.y = cur.ny;
					
					// 명령을 받은 기사는 피해를 입지 않음
					cur.k -= i == id ? 0 : cur.dmg;
					result[i] += i == id ? 0 : cur.dmg;
				}
			}
		}

		int answer = 0;
		
		for (int i = 1; i <= N; i++) {
			// 생존한 기사의 대미지 합을 구함
			if (knight[i].k > 0) {
				answer += result[i];
			}
		}
		
		System.out.println(answer);
	}
	
	private static boolean move(int num, int d) {
		Queue<Node> q = new LinkedList<>();
		boolean[] visited = new boolean[N + 1];
		
		q.add(knight[num]);
		visited[num] = true;
		
		while (!q.isEmpty()) {
			Node cur = q.poll();
			
			int nx = cur.x + dx[d];
			int ny = cur.y + dy[d];
			
			// 이동하려는데 범위를 벗어나는 경우 모든 기사가 이동할 수 없음
			if (!isRange(nx, ny, cur.h, cur.w)) {
				return false;
			}
			
			// 이동한 곳의 함정 개수 세기
			int cnt = 0;
			
			for (int i = nx; i < nx + cur.h; i++) {
				for (int j = ny; j < ny + cur.w; j++) {
					if (map[i][j] == 1) {
						cnt++;
					} else if (map[i][j] == 2) {
						return false;
					}
				}
			}
			
			cur.dmg = cnt;
			cur.nx = nx;
			cur.ny = ny;
			
			// 다른 기사와 겹치는지 확인
			for (int i = 1; i <= N; i++) {
				if (visited[i] || knight[i].k <= 0) {
					continue;
				}
				
				// 현재 이동 위치가 다른 기사의 범위보다 큰 경우 || 다른 기사 위치가 현재 이동한 범위보다 큰 경우
				// -> 겹치지 않는 것
				if (nx > knight[i].x + knight[i].h - 1 || knight[i].x > nx + cur.h - 1) {
					continue;
				}
				
				if (ny > knight[i].y + knight[i].w - 1 || knight[i].y > ny + cur.w - 1) {
					continue;
				}
				
				// 범위가 겹친다면 연쇄적으로 이동하기 위해 큐에 추가
				q.add(knight[i]);
				visited[i] = true;
			}
		}
		
		return true;
	}
	
	private static boolean isRange(int x, int y, int h, int w) {
		return x >= 0 && x + h - 1 < L && y >= 0 && y + w - 1 < L;
	}

}
