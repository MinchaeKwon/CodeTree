/**
 * 팩맨
 * 삼성 SW 역량테스트 2021 하반기 오후 1번 문제
 * https://www.codetree.ai/training-field/frequent-problems/problems/pacman/description?page=3&pageSize=20
 * 
 * @author minchae
 * @date 2023. 10. 18.
 * 
 * 백준 23290 마법사 상어와 복제
 * https://www.acmicpc.net/problem/23290
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer;

class Monster {
	int x;
	int y;
	int d;
	
	public Monster(int x, int y, int d) {
		this.x = x;
		this.y = y;
		this.d = d;
	}
}

public class Main {
	
	// 몬스터 방향 ↑, ↖, ←, ↙, ↓, ↘, →, ↗
	static int[] mdx = {-1, -1, 0, 1, 1, 1, 0, -1};
	static int[] mdy = {0, -1, -1, -1, 0, 1, 1, 1};
	
	// 팩맨 방향 상좌하우
	static int[] pdx = {-1, 0, 1, 0};
	static int[] pdy = {0, -1, 0, 1};
	
	static ArrayList<Integer>[][] map = new ArrayList[4][4]; // 몬스터 방향 저장
	static int[][] dead = new int[4][4]; // 몬스터 시체 저장
	
	static int px, py; // 팩맨 방향
	static int[] pacmanDir = new int[3]; // 팩맨이 이동하는 방향 저장
	static boolean[][] visited; // 팩맨이 이미 이동한 곳인지 확인
	
	static int eat; // 팩맥이 가장 많이 먹을 수 있는 몬스터의 수 저장

	public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        
        int m = Integer.parseInt(st.nextToken());
        int t = Integer.parseInt(st.nextToken());
        
        st = new StringTokenizer(br.readLine());
        px = Integer.parseInt(st.nextToken()) - 1;
        py = Integer.parseInt(st.nextToken()) - 1;
        
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
        	// 1. 몬스터 복제 시도
        	ArrayList<Monster> copy = copyMonster();
        	
        	// 2. 몬스터 이동
        	moveMoster();
        	
        	// 3. 팩맨 이동
        	movePacman();
        	
        	// 4. 몬스터 시체 소멸
        	removeMonster();
        	
        	// 5. 몬스터 복제 완성
        	setCopyMonster(copy);
        }
        
        
        int result = 0;
        
        for (int i = 0; i < 4; i++) {
        	for (int j = 0; j < 4; j++) {
        		result += map[i][j].size();
        	}
        }
        
        System.out.println(result);
	}
	
	// 1. 몬스터 복제 시도
	private static ArrayList<Monster> copyMonster() {
		ArrayList<Monster> copy = new ArrayList<>();
		
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				for (int d : map[i][j]) {
					copy.add(new Monster(i, j, d));
				}
			}
		}
		
		return copy;
	}
	
	// 2. 몬스터 이동
	private static void moveMoster() {
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
						
						// 범위를 벗어나지 않으며 몬스터 시체가 없고, 팩맨이 있는 곳이 아닌 경우
						if (isRange(nx, ny) && dead[nx][ny] == 0 && !(nx == px && ny == py)) {
							newMap[nx][ny].add(nd);
							move = true;
							
							break;
						}
					}
					
					// 8방향 다 이동할 수 없으면 이동하지 않음
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
		
		findDir(0, px, py, 0, new int[3]);
		
		for (int i = 0; i < 3; i++) {
			px += pdx[pacmanDir[i]];
			py += pdy[pacmanDir[i]];
			
			if (map[px][py].size() > 0) {
				map[px][py].clear();
				dead[px][py] = 3; // 죽은 몬스터는 시체를 남김
			}
		}
	}
	
	// 팩맨이 몬스터를 가장 많이 먹을 수 있는 방향 탐색
	private static void findDir(int depth, int x, int y, int cnt, int[] arr) {
		if (depth == 3) {
			// 최댓값 갱신 후 방향 저장
			if (cnt > eat) {
				pacmanDir = Arrays.copyOf(arr, 3);
				eat = cnt;
			}
			
			return;
		}
		
		for (int i = 0; i < 4; i++) {
			int nx = x + pdx[i];
			int ny = y + pdy[i];
			
			// 범위를 벗어나지 않는 경우
			if (isRange(nx, ny)) {
				arr[depth] = i;
				
				if (!visited[nx][ny]) {
					visited[nx][ny] = true;
					findDir(depth + 1, nx, ny, cnt + map[nx][ny].size(), arr);
					visited[nx][ny] = false;
				} else {
					findDir(depth + 1, nx, ny, cnt, arr); // 이미 방문한 칸일 경우 몬스터 개수 세지 않음
				}
			}
		}
	}
	
	// 4. 몬스터 시체 소멸
	private static void removeMonster() {
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				// 몬스터 시체가 남아있는 경우 1 감소
				if (dead[i][j] > 0) {
					dead[i][j]--;
				}
			}
		}
	}
	
	// 5. 몬스터 복제 완성 -> 복제된 몬스터 맵에 배치
	private static void setCopyMonster(ArrayList<Monster> copy) {
		for (Monster m : copy) {
			map[m.x][m.y].add(m.d);
		}
	}
	
	private static boolean isRange(int x, int y) {
		return x >= 0 && x < 4 && y >= 0 && y < 4;
	}
	
}
