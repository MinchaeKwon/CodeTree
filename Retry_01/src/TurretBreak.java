/**
 * 포탑 부수기
 * 
 * @author minchae
 * @date 2023. 12. 14.
 */

import java.io.*;
import java.util.*;

public class TurretBreak {
	
	static class Turret implements Comparable<Turret> {
		int x;
		int y;
		int power; // 공격력
		int time; // 공격시점
		
		public Turret(int x, int y, int power, int time) {
			this.x = x;
			this.y = y;
			this.power = power;
			this.time = time;
		}
		
		public Turret(int x, int y) {
			this.x = x;
			this.y = y;
		}
		
		@Override
		public int compareTo(TurretBreak.Turret o) {
			if (this.power != o.power) {
				return this.power - o.power;
			}
			
			if (this.time != o.time) {
				return o.time - this.time;
			}
			
			if (this.x + this.y != o.x + o.y) {
				return (o.x + o.y) - (this.x + this.y);
			}
			
			return o.y - this.y;
		}
	}
	
	// 우하좌상, 대각선
	static int[] dx = {0, 1, 0, -1, -1, -1, 1, 1};
	static int[] dy = {1, 0, -1, 0, -1, 1, -1, 1};
	
	static int N, M, K;
	
	static int[][] map;
	static boolean[][] effect; // 특정 턴에 공격을 받았는지 확인
	static int[][] time; // 공격 시점 저장
	
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		N = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());
		K = Integer.parseInt(st.nextToken());
		
		map = new int[N][M];
		time = new int[N][M];
		
		for (int i = 0; i < N; i++) {
			st = new StringTokenizer(br.readLine());
			
			for (int j = 0; j < M; j++) {
				map[i][j] = Integer.parseInt(st.nextToken());
			}
		}
		
		for (int t = 1; t <= K; t++) {
			// 포탑이 한 개 남은 경우 공격할 포탑이 없기 때문에 종료
			if (isFinish()) {
				break;
			}
			
			effect = new boolean[N][M];
			
			// 1. 공격력이 가장 높고 낮은 포탑 찾기
			ArrayList<Turret> list = new ArrayList<>();
			
			for (int i = 0; i < N; i++) {
				for (int j = 0; j < M; j++) {
					if (map[i][j] > 0) {
						list.add(new Turret(i, j, map[i][j], time[i][j]));
					}
				}
			}
			
			Collections.sort(list);
			
			Turret start = list.get(0); // 가장 약한 포탑
			Turret end = list.get(list.size() - 1); // 가장 강한 포탑
			
			// 공격자의 공격력 증가
			start.power += (N + M);
			map[start.x][start.y] = start.power;
			
			time[start.x][start.y] = t;
			start.time = t;
			
			effect[start.x][start.y] = true;
			
			// 2. 포탑 공격함
			if (!laser(start, end)) {
				bomb(start, end);
			}
			
			// 3. 포탑 정비
			for (int i = 0; i < N; i++) {
				for (int j = 0; j < M; j++) {
					// 부서지지 않았고, 공격과 무관했던 포탑일 경우 공격력 1 증가
					if (map[i][j] > 0 && !effect[i][j]) {
						map[i][j] += 1;
					}
				}
			}
		}
		
		int answer = 0;
		
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < M; j++) {
				answer = Math.max(answer, map[i][j]);
			}
		}
		
		// 가장 강한 포탑의 공격력 출력
		System.out.println(answer);

	}
	
	// 레이저 공격
	private static boolean laser(Turret start, Turret end) {
		Queue<Turret> q = new LinkedList<>();
		boolean[][] visited = new boolean[N][M];
		
		Turret[][] route = new Turret[N][M];
		
		q.add(start);
		visited[start.x][start.y] = true;
		
		boolean attack = false; // 공격 경로가 있는지 확인하는 변수
		
		// 공격 지점까지의 최단 경로 찾음
		while (!q.isEmpty()) {
			Turret cur = q.poll();
			
			// 공격 경로가 있는 경우 종료
			if (cur.x == end.x && cur.y == end.y) {
				attack = true;
				break;
			}
			
			for (int i = 0; i < 4; i++) {
				int nx = (N + cur.x + dx[i]) % N;
				int ny = (M + cur.y + dy[i]) % M;;
				
				// 아직 방문하지 않았고, 부서지지 않은 포탑인 경우
				if (!visited[nx][ny] && map[nx][ny] > 0) {
					q.add(new Turret(nx, ny));
					visited[nx][ny] = true;
					
					route[nx][ny] = cur; // 경로 저장
				}
			}
		}
		
		// 경로 역추적하면서 포탑 공격함
		if (attack) {
			map[end.x][end.y] -= start.power;
			effect[end.x][end.y] = true;
			
			Turret turret = route[end.x][end.y];
			int x = turret.x;
			int y = turret.y;
			
			while (!(x == start.x && y == start.y)) {
				map[x][y] -= start.power / 2;
				effect[x][y] = true;
				
				turret = route[x][y];
				x = turret.x;
				y = turret.y;
			}
		}
		
		return attack;
	}
	
	// 폭탄 공격
	private static void bomb(Turret start, Turret end) {
		map[end.x][end.y] -= start.power;
		effect[end.x][end.y] = true;
		
		for (int i = 0; i < 8; i++) {
			int nx = (N + end.x + dx[i]) % N;
			int ny = (M + end.y + dy[i]) % M;
			
			// 공격자의 위치가 아닌 경우
			if (!(nx == start.x && ny == start.y)) {
				map[nx][ny] -= start.power / 2;
				effect[nx][ny] = true;
			}
		}
	}
	
	// 포탑이 한 개 남았는지 확인
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

}
