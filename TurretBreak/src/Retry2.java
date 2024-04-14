/**
 * 포탑 부수기
 * 
 * @author minchae
 * @date 2024. 4. 14.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

public class Retry2 {
	
	static class Turret implements Comparable<Turret> {
		int x;
		int y;
		int power;
		int time;
		
		public Turret(int x, int y) {
			this.x = x;
			this.y = y;
		}
		
		public Turret(int x, int y, int power, int time) {
			this.x = x;
			this.y = y;
			this.power = power;
			this.time = time;
		}

		@Override
		public int compareTo(Retry2.Turret o) {
			if (this.power == o.power) {
				if (this.time == o.time) {
					if ((this.x + this.y) == (o.x + o.y)) {
						return o.y - this.y;
					}
					
					return (o.x + o.y) - (this.x + this.y);
				}
				
				return o.time - this.time;
			}
			
			return this.power - o.power;
		}
	}
	
	// 우하좌상, 대각선
	static int[] dx = {0, 1, 0, -1, -1, -1, 1, 1};
	static int[] dy = {1, 0, -1, 0, -1, 1, -1, 1};
	
	static int N, M, K;
	
	static int[][] map; // 포탑 공격력 저장
	static boolean[][] effect;
	static int[][] attack;

	public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        
        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        K = Integer.parseInt(st.nextToken());
        
        map = new int[N][M];
        attack = new int[N][M];
        
        for (int i = 0; i < N; i++) {
        	st = new StringTokenizer(br.readLine());
        	
        	for (int j = 0; j < M; j++) {
        		map[i][j] = Integer.parseInt(st.nextToken());
        	}
        }
        
        for (int t = 1; t <= K; t++) {
        	if (isFinish()) {
        		break;
        	}
        	
        	effect = new boolean[N][M];
        	
        	// 가장 약한 포탑 찾음
        	ArrayList<Turret> list = new ArrayList<>();
        	
        	for (int i = 0; i < N; i++) {
        		for (int j = 0; j < M; j++) {
        			if (map[i][j] > 0) {
        				list.add(new Turret(i, j, map[i][j], attack[i][j]));
        			}
        		}
        	}
        	
        	Collections.sort(list);
        	
        	Turret start = list.get(0); // 공격자
        	
        	start.power += (N + M);
        	map[start.x][start.y] = start.power;
        	
        	attack[start.x][start.y] = t;
        	effect[start.x][start.y] = true;
        	
        	Turret end = list.get(list.size() - 1); // 공격 대상
        	
        	if (!laser(start, end)) {
        		bomb(start, end);
        	}
        	
        	add();
        }

        int answer = 0;
        
        for (int i = 0; i < N; i++) {
        	for (int j = 0; j < M; j++) {
        		if (map[i][j] > 0) {
        			answer = Math.max(answer, map[i][j]);
        		}
        	}
        }
        
        System.out.println(answer);
	}
	
	private static boolean isFinish() {
		int cnt = 0;
		
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < M; j++) {
				if (map[i][j] > 0) {
					cnt++;
				}
			}
		}
		
		return cnt == 1;
	}
	
	private static boolean laser(Turret start, Turret end) {
		Queue<Turret> q = new LinkedList<>();
		boolean[][] visited = new boolean[N][M];
		
		q.add(start);
		visited[start.x][start.y] = true;
		
		boolean isAttack = false;
		
		Turret[][] route = new Turret[N][M];
		
		while (!q.isEmpty()) {
			Turret cur = q.poll();
			
			if (cur.x == end.x && cur.y == end.y) {
				isAttack = true;
				break;
			}
			
			for (int i = 0; i < 4; i++) {
				int nx = (N + cur.x + dx[i]) % N;
				int ny = (M + cur.y + dy[i]) % M;
				
				if (!visited[nx][ny] && map[nx][ny] > 0) {
					q.add(new Turret(nx, ny));
					visited[nx][ny] = true;
					
					route[nx][ny] = cur;
				}
			}
		}
		
		if (isAttack) {
			map[end.x][end.y] -= start.power;
			effect[end.x][end.y] = true;
			
			Turret cur = route[end.x][end.y];
			
			int x = cur.x;
			int y = cur.y;
			
			while (!(x == start.x && y == start.y)) {
				map[x][y] -= start.power / 2;
				effect[x][y] = true;
				
				x = route[x][y].x;
				y = route[x][y].y;
			}
		}
		
		return isAttack;
	}
	
	private static void bomb(Turret start, Turret end) {
		map[end.x][end.y] -= start.power;
		effect[end.x][end.y] = true;
		
		for (int i = 0; i < 8; i++) {
			int nx = (N + end.x + dx[i]) % N;
			int ny = (M + end.y + dy[i]) % M;
			
			if (nx == start.x && ny == start.y) {
				continue;
			}
			
			if (map[nx][ny] > 0) {
				map[nx][ny] -= start.power / 2;
				effect[nx][ny] = true;
			}
		}
	}
	
	private static void add() {
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < M; j++) {
				if (map[i][j] > 0 && !effect[i][j]) {
					map[i][j]++;
				}
			}
		}
	}

}
