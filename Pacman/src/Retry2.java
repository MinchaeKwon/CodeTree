/**
 * 팩맨
 * 
 * @author minchae
 * @date 2024. 4. 19.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer;

public class Retry2 {
	
	static class Monster {
		int x;
		int y;
		int d;
		
		public Monster(int x, int y, int d) {
			this.x = x;
			this.y = y;
			this.d = d;
		}
	}
	
	// 상좌하우
	static int[] pdx = {-1, 0, -1, 0};
	static int[] pdy = {0, -1, 0, 1};
	
	// ↑, ↖, ←, ↙, ↓, ↘, →, ↗ 
	static int[] mdx = {-1, -1, 0, 1, 1, 1, 0, -1};
	static int[] mdy = {0, -1, -1, -1, 0, 1, 1, 1};
	
	static int m, t;
	static int px, py;
	
	static ArrayList<Integer>[][] map; // 몬스터의 방향 저장
	static int[][] dead; // 몬스터 시체 소멸 턴 수 저장
	
	static int[] selected;
	static boolean[][] visited;
	
	static int eat;

	public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        
        m = Integer.parseInt(st.nextToken());
        t = Integer.parseInt(st.nextToken());
        
        st = new StringTokenizer(br.readLine());
        
        px = Integer.parseInt(st.nextToken()) - 1;
        py = Integer.parseInt(st.nextToken()) - 1;
        
        map = new ArrayList[4][4];
        dead = new int[4][4];
        
        for (int i = 0; i < 4; i++) {
        	for (int j = 0; j < 4; j++) {
        		map[i][j] = new ArrayList<>();
        	}
        }
        
        for (int i = 0; i < m; i++) {
        	st = new StringTokenizer(br.readLine());
        	
        	int r = Integer.parseInt(st.nextToken()) - 1;
        	int c = Integer.parseInt(st.nextToken()) - 1;
        	int d = Integer.parseInt(st.nextToken()) - 1;
        	
        	map[r][c].add(d);
        }

        while (t-- > 0) {
        	ArrayList<Monster> list = copyMonster();
        	moveMonster();
        	movePacman();
        	decrease();
        	complete(list);
        }
        
        int result = 0;
        
        for (int i = 0; i < 4; i++) {
        	for (int j = 0; j < 4; j++) {
        		result += map[i][j].size();
        	}
        }
        
        System.out.println(result);
	}
	
	private static ArrayList<Monster> copyMonster() {
		ArrayList<Monster> result = new ArrayList<>();
		
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				for (int d : map[i][j]) {
					result.add(new Monster(i, j, d));
				}
			}
		}
		
		return result;
	}
	
	private static void moveMonster() {
		ArrayList<Integer>[][] newMap = new ArrayList[4][4];
		
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				newMap[i][j] = new ArrayList<>();
			}
		}
		
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				for (int dir : map[i][j]) {
					boolean move = false;
					
					for (int d = 0; d < 8; d++) {
						int nd = (dir + d) % 8;
						
						int nx = i + mdx[nd];
						int ny = j + mdy[nd];
						
						if (isRange(nx, ny) && dead[nx][ny] == 0 && !(nx == px && ny == py)) {
							move = true;
							newMap[nx][ny].add(nd);
							
							break;
						}
					}
					
					if (!move) {
						newMap[i][j].add(dir);
					}
				}
			}
		}
		
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				map[i][j] = new ArrayList<>(newMap[i][j]);
			}
		}
	}
	
	private static void movePacman() {
		eat = -1;
		visited = new boolean[4][4];
		
		dfs(0, px, py, new int[3], 0);
		
		for (int i = 0; i < 3; i++) {
			px += pdx[selected[i]];
			py += pdy[selected[i]];
			
			if (!map[px][py].isEmpty()) {
				map[px][py].clear();
				dead[px][py] = 3;
			}
		}
	}
	
	private static void dfs(int depth, int x, int y, int[] arr, int sum) {
		if (depth == 3) {
			if (sum > eat) {
				eat = sum;
				selected = Arrays.copyOf(arr, 3);
			}
			
			return;
		}
		
		for (int i = 0; i < 4; i++) {
			int nx = x + pdx[i];
			int ny = y + pdy[i];
			
			if (isRange(nx, ny)) {
				arr[depth] = i;
				
				if (!visited[nx][ny]) {
					visited[nx][ny] = true;
					
					dfs(depth + 1, nx, ny, arr, sum + map[nx][ny].size());
					
					visited[nx][ny] = false;
				} else {
					dfs(depth + 1, nx, ny, arr, sum);
				}
			}
		}
	}
	
	private static void decrease() {
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if (dead[i][j] > 0) {
					dead[i][j]--;
				}
			}
		}
	}
	
	private static void complete(ArrayList<Monster> list) {
		for (Monster cur : list) {
			map[cur.x][cur.y].add(cur.d);
		}
	}
	
	private static boolean isRange(int x, int y) {
		return x >= 0 && x < 4 && y >= 0 && y < 4;
	}

}
