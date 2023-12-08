/**
 * 싸움땅
 * 삼성 SW 역량테스트 2022 하반기 오전 1번 문제
 * https://www.codetree.ai/training-field/frequent-problems/problems/battle-ground/description?page=1&pageSize=20
 * 
 * @author minchae
 * @date 2023. 10. 23.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.StringTokenizer;

class Player {
	int num;
	int x;
	int y;
	int d; // 방향
	int s; // 초기 능력치
	int g; // 플레이어가 가지고 있는 총의 공격력
	
	public Player(int num, int x, int y, int d, int s, int g) {
		this.num = num;
		this.x = x;
		this.y = y;
		this.d = d;
		this.s = s;
		this.g = g;
	}
	
	public Player(int x, int y) {
		this.x = x;
		this.y = y;
	}
}

public class Main {
	
	// 상우하좌(북동남서) -> 시계 방향으로 회전하기 편함
	static int[] dx = {-1, 0, 1, 0};
	static int[] dy = {0, 1, 0, -1};
	
	static int n, m, k;
	
	static ArrayList<Integer>[][] gun;
	static Player[] players;
	
	static int[] score; // 각 플레이어들의 점수 저장

	public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        k = Integer.parseInt(st.nextToken());
        
        gun = new ArrayList[n][n];
        players = new Player[m];
        score = new int[m];
        
        for (int i = 0; i < n; i++) {
        	st = new StringTokenizer(br.readLine());
        	
        	for (int j = 0; j < n; j++) {
        		gun[i][j] = new ArrayList<>();
        		
        		int num = Integer.parseInt(st.nextToken());
        		
        		if (num > 0) {
        			gun[i][j].add(num);	
        		}
        	}
        }
        
        for (int i = 0; i < m; i++) {
        	st = new StringTokenizer(br.readLine());
        	
        	int x = Integer.parseInt(st.nextToken()) - 1;
        	int y = Integer.parseInt(st.nextToken()) - 1;
        	int d = Integer.parseInt(st.nextToken()); // 초기 방향, 0부터 3까지 순서대로 ↑, →, ↓, ←
        	int s = Integer.parseInt(st.nextToken()); // 플레이어의 초기 능력치
        	
        	players[i] = new Player(i, x, y, d, s, 0);
        }
        
        // k라운드 동안 게임 진행
        while (k-- > 0) {
        	simulate();
        }
        
        // 각 플레이어들이 획득한 포인트를 출력
        for (int s : score) {
        	System.out.print(s + " ");
        }
	}
	
	private static void simulate() {
		for (int i = 0; i < m; i++) {
			int num = players[i].num;
			int x = players[i].x;
			int y = players[i].y;
			int d = players[i].d;
			int s = players[i].s;
			int g = players[i].g;
			
			// 해당 플레이어가 다음으로 이동할 방향 찾기
			int nx = x + dx[d];
			int ny = y + dy[d];
			
			// 격자를 벗어나는 경우 정방대 방향으로 바꿈
			if (!checkRange(nx, ny)) {
				d = (d + 2) % 4;
				
				nx = x + dx[d];
				ny = y + dy[d];
			}
			
			Player next = findPlayer(nx, ny); // 다음 위치에 있는 플레이어 정보 얻음
			
			// 플레이어가 이동하기 때문에 위치 갱신 시켜줌
			Player cur = new Player(num, nx, ny, d, s, g);
			update(cur);
			
			if (next == null) { // 이동한 방향에 플레이어가 없는 경우
				move(cur, nx, ny);
			} else { // 이동한 방향에 다른 플레이어가 있는 경우
				fight(cur, next, nx, ny);
			}
			
		}
	}
	
	// 해당 칸에 있는 플레이어 정보 찾음
	private static Player findPlayer(int x, int y) {
		for (int i = 0; i < m; i++) {
			if (players[i].x == x && players[i].y == y) {
				return players[i];
			}
		}

		return null;
	}
	
	// 플레이어 정보 갱신
	private static void update(Player update) {
		for (int i = 0; i < m; i++) {
			if (players[i].num == update.num) {
				players[i] = update;
				break;
			}
		}
	}
	
	// 가장 좋은 총으로 갱신하고 플레이어 이동시킴
	private static void move(Player p, int nx, int ny) {
		// 가장 좋은 총으로 갱신
		gun[nx][ny].add(p.g);
		Collections.sort(gun[nx][ny]);
		
		p.g = gun[nx][ny].get(gun[nx][ny].size() - 1);
		gun[nx][ny].remove(gun[nx][ny].size() - 1);

		p = new Player(p.num, nx, ny, p.d, p.s, p.g);
		update(p);
	}
	
	// 두 명의 플레이어가 싸움
	private static void fight(Player p1, Player p2, int nx, int ny) {
		int attack1 = p1.s + p1.g;
		int attack2 = p2.s + p2.g;
		
		if (attack1 > attack2 || (attack1 == attack2 && p1.s > p2.s)) { // p1이 이긴 경우
			score[p1.num] += attack1 - attack2;
			loseMove(p2);
			move(p1, nx, ny);
		} else { // p2가 이긴 경우
			score[p2.num] += attack2 - attack1;
			loseMove(p1);
			move(p2, nx, ny);
		}
	}
	
	// 진 플레이어가 이동하는 함수
	private static void loseMove(Player p) {
		gun[p.x][p.y].add(p.g); // 총을 해당 칸에 내려놓음
		
		for (int i = 0; i < 4; i++) {
			int nd = (p.d + i) % 4; // 플레이어가 가지고 있던 방향부터 4방향 탐색 (시계 방향 90도 회전)
			
			int nx = p.x + dx[nd];
			int ny = p.y + dy[nd];
			
			if (checkRange(nx, ny) && findPlayer(nx, ny) == null) {
				move(new Player(p.num, p.x, p.y, nd, p.s, 0), nx, ny);
				break;
			}
		}
	}
	
	private static boolean checkRange(int x, int y) {
		return x >= 0 && x < n && y >= 0 && y < n;
	}

}
