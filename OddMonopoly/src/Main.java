/**
 * 승자독식 모노폴리
 * 삼성 SW 역량테스트 2020 상반기 오후 1번 문제
 * https://www.codetree.ai/training-field/frequent-problems/problems/odd-monopoly/description?page=2&pageSize=20
 * 
 * @author minchae
 * @date 2023. 10. 5.
 * 
 * 백준 19237 어른 상어
 * https://www.acmicpc.net/problem/19237
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

class Player {
	int x;
	int y;
	int n;
	int d;
	int[][] priority = new int[4][4]; // 상하좌우 각 방향의 이동 우선순위
	
	public Player(int x, int y, int n) {
		this.x = x;
		this.y = y;
		this.n = n;
	}
}

public class Main {
	
	static int[] dx = {-1, 1, 0, 0};
	static int[] dy = {0, 0, -1, 1};
	
	static int n, m, k;
	
	static int[][] map; // 플레이어 위치
	static int[][] owner; // 독점계약한 플레이어 번호 저장
	static int[][] turn; // 플레이어가 독점했을 경우 남은 턴의 수
	static HashMap<Integer, Player> playerMap = new HashMap<>(); // 플레이어 정보 저장
	
	static int cnt = 0;

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());

		n = Integer.parseInt(st.nextToken());
		m = Integer.parseInt(st.nextToken());
		k = Integer.parseInt(st.nextToken());
		
		map = new int[n][n];
		owner = new int[n][n];
		turn = new int[n][n];
		
		// 격자 정보
		for (int i = 0; i < n; i++) {
			st = new StringTokenizer(br.readLine());
			
			for (int j = 0; j < n; j++) {
				map[i][j] = Integer.parseInt(st.nextToken());
				
				if (map[i][j] > 0) {
					owner[i][j] = map[i][j];
					turn[i][j] = k;
					
					playerMap.put(map[i][j], new Player(i, j, map[i][j]));
				}
			}
		}
		
		// 각 플레이어의 초기 방향
		st = new StringTokenizer(br.readLine());
		for (int i = 1; i <= m; i++) {
			playerMap.get(i).d = Integer.parseInt(st.nextToken()) - 1;
		}
		
		// 각 플레이어의 방향에 따른 이동 우선순위
		for (int i = 1; i <= m; i++) {
			for (int j = 0; j < 4; j++) {
				st = new StringTokenizer(br.readLine());
				
				for (int k = 0; k < 4; k++) {
					playerMap.get(i).priority[j][k] = Integer.parseInt(st.nextToken()) - 1;
				}
			}
		}
		
		playGame();
	}
	
	// 게임 진행
	private static void playGame() {
		while (cnt < 1000) {
			movePlayer();
			decreaseTurnCnt();
			exclusiveContract();
			
			cnt++;
			
			// 같은 칸에 플레이어가 여러 명 있을 경우 번호가 큰 플레이어는 사라지기 때문에 플레이어맵 크기가 1인 경우는 1번 플레이어만 살아남은 경우임
			if (playerMap.size() == 1) {
				System.out.println(cnt); // 턴의 수 출력
				return;
			}
		}
		
		System.out.println(-1);
	}
	
	// 1. 플레이어 한 칸씩 이동시키기
	private static void movePlayer() {
		ArrayList<Integer> remove = new ArrayList<>(); // 사라지는 플레이어 번호 저장
		
		for (Player player : playerMap.values()) {
			ArrayList<Integer> empty = new ArrayList<>(); // 플레이어에 인접한 빈 칸 리스트
			ArrayList<Integer> my = new ArrayList<>(); // 플레이어에 인접한 본인의 땅 리스트
			
			// 인접한 4방향 탐색하면서 이동가능한 후보칸 찾음
			for (int i = 0; i < 4; i++) {
				int nx = player.x + dx[i];
				int ny = player.y + dy[i];
				
				if (nx >= 0 && nx < n && ny >= 0 && ny < n) {
					if (owner[nx][ny] == 0) { // 빈 칸인 경우
						empty.add(i);
					} else if (owner[nx][ny] == player.n) { // 자신이 독점계약한 땅인 경우
						my.add(i);
					}
				}
			}
			
			int nd = findNextDir(player, empty); // 빈 칸 중에서 이동할 수 있는 방향 확인
			
			 // 빈 칸 중에 이동할 수 있는 방향 없는 경
			if (nd == -1) {
				nd = findNextDir(player, my); // 본인이 독점계약한 땅으로 이동할 수 있는 방향 확인
			}
			
			map[player.x][player.y] = 0; // 플레이어가 원래 있는 위치는 0으로 바꿔줌
			
			// 다음 방향으로 플레이어 위치 옮겨줌
			switch (nd) {
			case 0: // 상
				player.x--;
				break;
			case 1: // 하
				player.x++;
				break;
			case 2: // 좌
				player.y--;
				break;
			case 3: // 우
				player.y++;
				break;
			}
			
			// 이동할 칸이 빈 칸인 경우 or 이동할 칸에 있는 플레이어 번호보다 현재 플레이어의 번호가 더 작을 경우
			if (map[player.x][player.y] == 0 || map[player.x][player.y] > player.n) {
				map[player.x][player.y] = player.n; // 이동한 위치에 플레이어 번호 넣음
				player.d = nd;
			} else {
				remove.add(player.n);
			}
			
		}
		
		// 플레이어 삭제
		for (int num : remove) {
			playerMap.remove(num);
		}
	}
	
	// 플레이어가 이동할 다음 방향 구하기
	private static int findNextDir(Player player, ArrayList<Integer> candidate) {
		// 해당 플레이어가 특정 방향일 때의 이동 우선순위를 탐색하면서 이동방향을 결정함
		for (int i = 0; i < 4; i++) {
			if (candidate.contains(player.priority[player.d][i])) {
				return player.priority[player.d][i];
			}
		}
		
		return -1; // 후보칸 리스트에 이동 가능한 칸이 없을 경우 -1 반환
	}
	
	// 2. 각 칸의 턴의 수 감소시키기 (독점계약 하기 전에 감소시켜야 올바른 답이 나옴)
	private static void decreaseTurnCnt() {
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (turn[i][j] > 0) {
					turn[i][j]--; //  턴의 수 감소시킴
					
					// 감소시켰는데 턴의 가 0이 돼서 독점계약이 끝난 경우 독점계약 번호판에서 플레이어 번호 지움
					if (turn[i][j] == 0) {
						owner[i][j] = 0;
					}
				}
			}
		}
	}
	
	// 3. 특정 칸 독점계약 하기 -> 플레이어가 한 칸씩 이동하고 해당 칸을 독점함
	private static void exclusiveContract() {
		for (Player player : playerMap.values()) {
			owner[player.x][player.y] = player.n;
			turn[player.x][player.y] = k;
		}
	}

}
