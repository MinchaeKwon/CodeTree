/**
 * 싸움땅
 * 
 * @author minchae
 * @date 2024. 4. 11.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.StringTokenizer;

public class Retry2 {
	
	static class Node {
		int n;
		int x;
		int y;
		int d; // 방향
		int s; // 초기 능력치
		int g; // 총의 공격력
		
		public Node(int n, int x, int y, int d, int s, int g) {
			this.n = n;
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
	static ArrayList<Integer>[][] map;
	static Node[] players;
	static int[] score;

	public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        k = Integer.parseInt(st.nextToken());
        
        map = new ArrayList[n][n];
        players = new Node[m + 1];
        score = new int[m + 1];
        
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
        
        for (int i = 1; i <= m; i++) {
        	st = new StringTokenizer(br.readLine());
        	
        	int x = Integer.parseInt(st.nextToken()) - 1;
        	int y = Integer.parseInt(st.nextToken()) - 1;
        	int d = Integer.parseInt(st.nextToken());
        	int s = Integer.parseInt(st.nextToken());
        	
        	players[i] = new Node(i, x, y, d, s, 0);
        }
        
        while (k-- > 0) {
        	play();
        }

        for (int i = 1; i <= m; i++) {
        	System.out.print(score[i] + " ");
        }
	}
	
	private static void play() {
		for (int i = 1; i <= m; i++) {
			Node cur = players[i];
			
			int nx = cur.x + dx[cur.d];
			int ny = cur.y + dy[cur.d];
			
			// 격자를 벗어나는 경우 정반대 방향으로 바꿈
			if (!isRange(nx, ny)) {
				cur.d = (cur.d + 2) % 4;
				
				nx = cur.x + dx[cur.d];
				ny = cur.y + dy[cur.d];
			}
			
			Node next = findPlayer(nx, ny); // 이동할 칸에 플레이어가 있는지 확인
			
			// 현재 플레이어 위치 갱신
			cur.x = nx;
			cur.y = ny;
			
			if (next == null) {
				getGun(cur);
			} else {
				fight(cur, next);
			}
		}
	}
	
	private static Node findPlayer(int x, int y) {
		for (int i = 1; i <= m; i++) {
			if (players[i].x == x && players[i].y == y) {
				return players[i];
			}
		}
		
		return null;
	}
	
	private static void getGun(Node p) {
		// 총을 가지고 있는 경우 해당 칸에 총을 내려놓음
		if (p.g > 0) {
			map[p.x][p.y].add(p.g);
		}
		
		if (!map[p.x][p.y].isEmpty()) {
			Collections.sort(map[p.x][p.y]);
			
			p.g = map[p.x][p.y].get(map[p.x][p.y].size() - 1); // 플레이어가 공격력 강한 총 획득
			map[p.x][p.y].remove(map[p.x][p.y].size() - 1); // 맵에서 총 삭제
		}
	}
	
	private static void fight(Node p1, Node p2) {
		int attack1 = p1.s + p1.g;
		int attack2 = p2.s + p2.g;
		
		if (attack1 > attack2 || (attack1 == attack2 && p1.s > p2.s)) {
			score[p1.n] += attack1 - attack2;
			
			// 진 사람이 먼저 움직임
			loseMove(p2);
			getGun(p1);
		} else {
			score[p2.n] += attack2 - attack1;
			loseMove(p1);
			getGun(p2);
		}
	}
	
	private static void loseMove(Node p) {
		// 총을 해당 칸에 내려놓음
		if (p.g > 0) {
			map[p.x][p.y].add(p.g);
			p.g = 0;
		}
		
		for (int i = 0; i < 4; i++) {
			int d = (p.d + i) % 4;
			
			int nx = p.x + dx[d];
			int ny = p.y + dy[d];
			
			if (isRange(nx, ny) && findPlayer(nx, ny) == null) {
				// 위치, 방향 갱신
				p.x = nx;
				p.y = ny;
				p.d = d;
				
				getGun(p);
				
				break;
			}
		}
	}
	
	private static boolean isRange(int x, int y) {
		return x >= 0 && x < n && y >= 0 && y < n;
	}

}
