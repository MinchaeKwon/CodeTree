/**
 * 팩맨
 * 
 * @author minchae
 * @date 2024. 3. 30.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer;

public class Retry1 {
	
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
	static int[] pdx = {-1, 0, 1, 0};
	static int[] pdy = {0, -1, 0, 1};
	
	static int[] mdx = {-1, -1, 0, 1, 1, 1, 0, -1};
	static int[] mdy = {0, -1, -1, -1, 0, 1, 1, 1};
	
	static int m, t;
	static int px, py;
	
	static ArrayList<Integer>[][] map;
	static int[][] dead;
	
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
    		
    		int x = Integer.parseInt(st.nextToken()) - 1;
    		int y = Integer.parseInt(st.nextToken()) - 1;
    		int d = Integer.parseInt(st.nextToken()) - 1;
    		
    		map[x][y].add(d);
    	}
    	
        while (t-- > 0) {
        	ArrayList<Monster> monster = copyMonster();
        	moveMonster();
        	movePacman();
        	removeMonster();
        	complete(monster);
        }
        
        int answer = 0;
        
        for (int i = 0; i < 4; i++) {
        	for (int j = 0; j < 4; j++) {
        		answer += map[i][j].size();
        	}
        }
        
        System.out.println(answer);
	}
	
	// 1. 몬스터 복제 시도
	private static ArrayList<Monster> copyMonster() {
		ArrayList<Monster> list = new ArrayList<>();
		
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				for (int d : map[i][j]) {
					list.add(new Monster(i, j, d));
				}
			}
		}
		
		return list;
	}
	
	// 2. 몬스터 이동
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
							newMap[nx][ny].add(nd);
							move = true;
							
							break;
						}
					}
					
					if (!move) {
						newMap[i][j].add(dir);
					}
				}
			}
		}
		
		map = newMap;
	}
	
	// 3. 팩맨 이동
	private static void movePacman() {
		visited = new boolean[4][4];
		eat = -1;
		
		dfs(0, new int[3], px, py, 0);
		
		for (int i = 0; i < 3; i++) {
			px += pdx[selected[i]];
			py += pdy[selected[i]];
			
			if (!map[px][py].isEmpty()) {
				map[px][py].clear();
				dead[px][py] = 3;
			}
		}
	}
	
	private static void dfs(int depth, int[] arr, int x, int y, int sum) {
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
					dfs(depth + 1, arr, nx, ny, sum + map[nx][ny].size());
					visited[nx][ny] = false;
				} else {
					dfs(depth + 1, arr, nx, ny, sum);
				}
			}
		}
	}
	
	// 4. 몬스터 시체 소멸
	private static void removeMonster() {
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if (dead[i][j] > 0) {
					dead[i][j]--;
				}
			}
		}
	}
	
	// 5. 몬스터 복제 완료
	private static void complete(ArrayList<Monster> copy) {
		for (Monster m : copy) {
			map[m.x][m.y].add(m.d);
		}
	}
	
	private static boolean isRange(int x, int y) {
		return x >= 0 && x < 4 && y >= 0 && y < 4;
	}

}
