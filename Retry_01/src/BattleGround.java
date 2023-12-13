/**
 * 싸움땅
 * 
 * @author minchae
 * @date 2023. 12. 13.
 */

import java.io.*;
import java.util.*;

public class BattleGround {
	
	static class Player {
		int num;
		int x;
		int y;
		int d; // 방향
		int s; // 플레이어 능력치
		int g; // 플레이어가 가지고 있는 총의 공격력
		
		public Player(int num, int x, int y, int d, int s, int g) {
			this.num = num;
			this.x = x;
			this.y = y;
			this.d = d;
			this.s = s;
			this.g = g;
		}
	}
	
	// 상우하좌
	static int[] dx = {-1, 0, 1, 0};
	static int[] dy = {0, 1, 0, -1};
	
	static int n, m, k;
	
	static ArrayList<Integer>[][] map; // 격자에 있는 총의 정보
	static Player[] players;
	
	static int[] score;

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		n = Integer.parseInt(st.nextToken());
		m = Integer.parseInt(st.nextToken());
		k = Integer.parseInt(st.nextToken());
		
		map = new ArrayList[n][n];
		players = new Player[m];
		score = new int[m];
		
		for (int i = 0; i < n; i++) {
			st = new StringTokenizer(br.readLine());
			
			for (int j = 0; j < n; j++) {
				map[i][j] = new ArrayList<>();
				
				int gun = Integer.parseInt(st.nextToken());
				
				// 해당 칸에 총이 있을 경우에만 저장
				if (gun > 0) {
					map[i][j].add(gun);
				}
			}
		}
		
		for (int i = 0; i < m; i++) {
			st = new StringTokenizer(br.readLine());
			
			int x = Integer.parseInt(st.nextToken()) - 1;
			int y = Integer.parseInt(st.nextToken()) - 1;
			int d = Integer.parseInt(st.nextToken());
			int s = Integer.parseInt(st.nextToken());
			
			players[i] = new Player(i, x, y, d, s, 0);
		}
		
		while (k-- > 0) {
			simulate();
		}

		// 각 플레이어가 획득한 포인트 출력
		for (int i : score) {
			System.out.print(i + " ");
		}
	}
	
	private static void simulate() {
		for (Player p : players) {
			int num = p.num;
			int x = p.x;
			int y = p.y;
			int d = p.d;
			int s = p.s;
			int g = p.g;
			
			// 플레이어가 이동할 다음 방향 구하기
			int nx = x + dx[d];
			int ny = y + dy[d];
			
			// 격자를 벗어나는 경우에는 정반대 방향으로 바꿈
			if (!isRange(nx, ny)) {
				d = (d + 2) % 4;
				
				nx = x + dx[d];
				ny = y + dy[d];
			}
			
			Player next = findPlayer(nx, ny); // 이동하려는 칸에 다른 플레이어가 있는지 확인
			
			/*
			 * 현재 플레이어가 다른 플레이어가 있는 곳으로 갔을 때
			 * 이동한 칸에서 진 경우 해당 칸에서 다음 방향으로 한 칸 이동해야 하기 때문에 여기서
			 * 미리 업데이트 해줌
			 */
			Player cur = new Player(num, nx, ny, d, s, g);
			updatePlayer(cur);
			
			if (next == null) {
				getGun(cur, nx, ny);
			} else {
				fight(cur, next, nx, ny);
			}
		}
	}
	
	// 특정 칸에 있는 플레이어 정보 반환
	private static Player findPlayer(int x, int y) {
		for (Player p : players) {
			if (p.x == x && p.y == y) {
				return p;
			}
		}
		
		return null;
	}
	
	private static void getGun(Player p, int x, int y) {
		// 플레이어가 총을 가지고 있을 경우에만 맵에 추가
		if (p.g > 0) {
			map[x][y].add(p.g);
		}
		
		// 해당 칸에 총이 있을 때만 플레이어가 총을 가지게 됨
		if (map[x][y].size() > 0) {
			// 가장 좋은 총 찾기
			Collections.sort(map[x][y]);
			
			p.g = map[x][y].get(map[x][y].size() - 1); // 해당 플레이어가 공격력이 가장 높은 총을 가지게 됨
			map[x][y].remove(map[x][y].size() - 1); // 해당 총은 맵에서 삭제
		}
		
		// 플레이어 정보 업데이트
		Player update = new Player(p.num, x, y, p.d, p.s, p.g);
		updatePlayer(update);
	}
	
	private static void updatePlayer(Player update) {
		for (int i = 0; i < m; i++) {
			if (i == update.num) {
				players[i] = update; // 참조값을 수정해야 하기 때문에 향상된 for문을 사용하지 않음
				return;
			}
		}
	}
	
	private static void fight(Player p1, Player p2, int x, int y) {
		int attack1 = p1.s + p1.g;
		int attack2 = p2.s + p2.g;
		
		if (attack1 > attack2 || (attack1 == attack2 && p1.s > p2.s)) {
			score[p1.num] += attack1 - attack2;
			
			lose(p2);
			getGun(p1, x, y);
		} else {
			score[p2.num] += attack2 - attack1;
			
			lose(p1);
			getGun(p2, x, y);
		}
	}
	
	private static void lose(Player p) {
		map[p.x][p.y].add(p.g);
		p.g = 0; // 총을 내려놨으니까 0을 넣어줌
		
		for (int i = 0; i < 4; i++) {
			int nd = (p.d + i) % 4;
			
			int nx = p.x + dx[nd];
			int ny = p.y + dy[nd];
			
			if (isRange(nx, ny) && findPlayer(nx, ny) == null) {
				p.d = nd; // 바꾼 방향 저장
				getGun(p, nx, ny);
				
				return;
			}
		}
	}
	
	private static boolean isRange(int x, int y) {
		return x >= 0 && x < n && y >= 0 && y < n;
	}

}
