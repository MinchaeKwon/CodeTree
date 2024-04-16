/**
 * 승자독식 모노폴리
 * 
 * @author minchae
 * @date 2024. 4. 16.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

public class Retry1 {
	
	static class Node {
		int num;
		int x;
		int y;
		int d;
		int[][] priority = new int[4][4];
		
		public Node(int num, int x, int y) {
			this.num = num;
			this.x = x;
			this.y = y;
		}
	}
	
	// 상하좌우
	static int[] dx = {-1, 1, 0, 0};
	static int[] dy = {0, 0, -1, 1};
	
	static int n, m, k;
	
	static int[][] map; // 플레이어 위치 저장
	static int[][] owner; // 독점계약한 플레이어 번호 저장
	static int[][] turn; // 남은 턴 수
	
	static HashMap<Integer, Node> hm = new HashMap<>(); // 플레이어 정보 저장
	
	static int answer;

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		n = Integer.parseInt(st.nextToken());
		m = Integer.parseInt(st.nextToken());
		k = Integer.parseInt(st.nextToken());
		
		map = new int[n][n];
		owner = new int[n][n];
		turn = new int[n][n];
		
		for (int i = 0; i < n; i++) {
			st = new StringTokenizer(br.readLine());
			
			for (int j = 0; j < n; j++) {
				map[i][j] = Integer.parseInt(st.nextToken());
				
				if (map[i][j] > 0) {
					hm.put(map[i][j], new Node(map[i][j], i, j));
					owner[i][j] = map[i][j];
					turn[i][j] = k;
				}
			}
		}
		
		st = new StringTokenizer(br.readLine());
		
		for (int i = 1; i <= m; i++) {
			hm.get(i).d = Integer.parseInt(st.nextToken()) - 1;
		}
		
		for (int i = 1; i <= m; i++) {
			for (int j = 0; j < 4; j++) {
				st = new StringTokenizer(br.readLine());
				
				for (int k = 0; k < 4; k++) {
					hm.get(i).priority[j][k] = Integer.parseInt(st.nextToken()) - 1;	
				}
			}
		}
		
		int cnt = 0;
		
		while (cnt < 1000) {
			move();
			decrease();
			setOwner();
			
			cnt++;
			
			if (hm.size() == 1) {
				System.out.println(cnt);
				return;
			}
		}

		System.out.println(-1);
	}
	
	private static void move() {
		ArrayList<Integer> remove = new ArrayList<>();
		
		for (Node cur : hm.values()) {
			ArrayList<Integer> empty = new ArrayList<>();
			ArrayList<Integer> my = new ArrayList<>();
			
			for (int i = 0; i < 4; i++) {
				int nx = cur.x + dx[i];
				int ny = cur.y + dy[i];
				
				if (!isRange(nx, ny)) {
					continue;
				}
				
				if (owner[nx][ny] == 0) {
					empty.add(i);
				} else if (owner[nx][ny] == cur.num) {
					my.add(i);
				}
			}
			
			int dir = findDir(cur, empty);
			
			if (dir == -1) {
				dir = findDir(cur, my);
			}
			
			map[cur.x][cur.y] = 0;
			
			cur.x += dx[dir];
			cur.y += dy[dir];
			
			if (map[cur.x][cur.y] == 0 || cur.num < map[cur.x][cur.y]) {
				map[cur.x][cur.y] = cur.num;
				cur.d = dir;
			} else {
				remove.add(cur.num);
			}
		}
		
		for (int num: remove) {
			hm.remove(num);
		}
	}
	
	private static int findDir(Node node, ArrayList<Integer> dirList) {
		for (int i = 0; i < 4; i++) {
			if (dirList.contains(node.priority[node.d][i])) {
				return node.priority[node.d][i];
			}
		}
		
		return -1;
	}
	
	private static void setOwner() {
		for (Node cur : hm.values()) {
			owner[cur.x][cur.y] = cur.num;
			turn[cur.x][cur.y] = k;
		}
	}
	
	private static void decrease() {
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (turn[i][j] > 0) {
					turn[i][j]--;
					
					if (turn[i][j] == 0) {
						owner[i][j] = 0;
					}
				}
			}
		}
	}
	
	private static boolean isRange(int x, int y) {
		return x >= 0 && x < n && y >= 0 && y < n;
	}

}
