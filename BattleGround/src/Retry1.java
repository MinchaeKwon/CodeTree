/**
 * 싸움땅
 * 
 * @author minchae
 * @date 2024. 3. 28.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.StringTokenizer;

public class Retry1 {
	
	static class Player {
		int num;
		int x;
		int y;
		int d;
		int s;
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
	
	static ArrayList<Integer>[][] map; // 총 저장
	static Player[] players;
	
	static int[] score;

	public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        k = Integer.parseInt(st.nextToken());
        
        map = new ArrayList[n][n];
        
        for (int i = 0; i < n; i++) {
        	st = new StringTokenizer(br.readLine());
        	
        	for (int j = 0; j < n; j++) {
        		map[i][j] = new ArrayList<>();
        		
        		int num = Integer.parseInt(st.nextToken());
        		
        		if (num > 0) {
        			map[i][j].add(num);	
        		}
        	}
        }
        
        players = new Player[m + 1];
        score = new int[m + 1];
        
        for (int i = 1; i <= m; i++) {
        	st = new StringTokenizer(br.readLine());
        	
        	int x = Integer.parseInt(st.nextToken()) - 1;
        	int y = Integer.parseInt(st.nextToken()) - 1;
        	int d = Integer.parseInt(st.nextToken());
        	int s = Integer.parseInt(st.nextToken());
    
        	players[i] = new Player(i, x, y, d, s, 0);
        }
        
        while (k-- > 0) {
        	play();
        }

        for (int i = 1; i <= m; i++) {
        	System.out.print(score[i] + " ");
        }
	}

	// 모든 플레이어 한 칸씩 이동
	private static void play() {
		for (int i = 1; i <= m; i++) {
			Player p = players[i];
			
			int nx = p.x + dx[p.d];
			int ny = p.y + dy[p.d];
			
			// 격자를 벗어나는 경우 정반대 방향으로 이동
			if (!isRange(nx, ny)) {
				p.d = (p.d + 2) % 4;
				
				nx = p.x + dx[p.d];
				ny = p.y + dy[p.d];
			}
			
			Player next = findPlayer(nx, ny); // 플레이어가 이동할 칸에 있는 플레이어 구함
			
			// 현재 플레이어 위치 갱신
			p.x = nx;
			p.y = ny;
			
			if (next == null) { // 플레이어가 없는 경우
				changeGun(p);
			} else { // 다른 플레이어가 있는 경우
				fight(p, next);
			}
		}
	}
	
	// 해당 칸에 있는 플레이어 구함
	private static Player findPlayer(int x, int y) {
		for (int i = 1; i <= m; i++) {
			Player p = players[i];
			
			if (p.x == x && p.y == y) {
				return p;
			}
		}
		
		return null;
	}
	
	// 공격력이 가장 강한 총을 얻음
	private static void changeGun(Player p) {
		// 플레이어가 총을 가지고 있는 경우
		if (p.g > 0) {
			map[p.x][p.y].add(p.g);	// 플레이어가 현재 가지고 있는 총을 맵에 내려놓음
		}
		
		if (!map[p.x][p.y].isEmpty()) {
			Collections.sort(map[p.x][p.y]);
			
			p.g = map[p.x][p.y].get(map[p.x][p.y].size() - 1); // 공격력이 가장 높은 총
			map[p.x][p.y].remove(map[p.x][p.y].size() - 1); // 플레이어가 가져간 총은 맵에서 삭제
		}
	}
	
	// 두 플레이어가 싸움
	private static void fight(Player p1, Player p2) {
		int attack1 = p1.s + p1.g;
		int attack2 = p2.s + p2.g;
		
		if (attack1 > attack2 || (attack1 == attack2 && p1.s > p2.s)) { // p1이 이김
			score[p1.num] += (attack1 - attack2);
			lose(p2);
			changeGun(p1);
		} else { // p2가 이김
			score[p2.num] += (attack2 - attack1);
			lose(p1);
			changeGun(p2);
		}
	}
	
	// 진 사람 이동
	private static void lose(Player p) {
		map[p.x][p.y].add(p.g); // 총을 해당 칸에 내려놓음
		p.g = 0;
		
		// 현재 방향 포함해서 시계 방향으로 전환하면서 빈 칸인지 확인
		for (int i = 0; i < 4; i++) {
			int nd = (p.d + i) % 4;
			
			int nx = p.x + dx[nd];
			int ny = p.y + dy[nd];
			
			// 범위 안이고 빈 칸인 경우
			if (isRange(nx, ny) && findPlayer(nx, ny) == null) {
				// 플레이어 위치, 방향 갱신
				p.x = nx;
				p.y = ny;
				p.d = nd;
				
				changeGun(p);
				
				break; // 이동했기 때문에 for문 종료
			}
		}
	}
	
	private static boolean isRange(int x, int y) {
		return x >= 0 && x < n && y >= 0 && y < n;
	}
	
}
