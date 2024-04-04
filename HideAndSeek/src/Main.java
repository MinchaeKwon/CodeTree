/**
 * 술래잡기
 * 삼성 SW 역량테스트 2022 상반기 오전 1번 문제
 * https://www.codetree.ai/training-field/frequent-problems/problems/hide-and-seek/description?page=1&pageSize=20
 * 
 * @author minchae
 * @date 2024. 4. 4.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Main {
	
	// 상우하좌
	static int[] dx = {-1, 0, 1, 0};
	static int[] dy = {0, 1, 0, -1};
	
	static int[] dc = {1, 1, 2, 2};
	
	static int n, m, h, k;
	
	static int[][] taggerDir;
	static int[][] taggerRevDir;
	
	static ArrayList<Integer>[][] map; // 도망자의 방향 저장 (도망자의 위치는 겹칠 수 있음)
	static ArrayList<Integer>[][] newMap; // 도망자가 이동한 방향 저장
	
	static boolean[][] tree;
	
	static int sx, sy; // 술래의 위치
	static boolean reverse; // 술래가 이동하는 방향이 정방향인지 역방향인지 확인
	
	static int answer;

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		n = Integer.parseInt(st.nextToken());
		m = Integer.parseInt(st.nextToken());
		h = Integer.parseInt(st.nextToken());
		k = Integer.parseInt(st.nextToken());
		
		map = new ArrayList[n][n];
		newMap = new ArrayList[n][n];
		tree = new boolean[n][n];
		
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				map[i][j] = new ArrayList<>();
			}
		}
		
		// 도망자의 위치
		for (int i = 0; i < m; i++) {
			st = new StringTokenizer(br.readLine());
			
			int x = Integer.parseInt(st.nextToken()) - 1;
			int y = Integer.parseInt(st.nextToken()) - 1;
			int d = Integer.parseInt(st.nextToken()); // 1 좌우, 2 상하
			
			map[x][y].add(d);
		}
		
		// 나무의 위치
		for (int i = 0; i < h; i++) {
			st = new StringTokenizer(br.readLine());
			
			int x = Integer.parseInt(st.nextToken()) - 1;
			int y = Integer.parseInt(st.nextToken()) - 1;
			
			tree[x][y] = true;
		}
		
		// 술래 시작 위치
		sx = n / 2;
		sy = n / 2;
		
		initTaggerDir();
		
		for (int t = 1; t <= k; t++) {
			moveFugitive();
			moveTagger();
			catchFugitive(t);
		}

		System.out.println(answer);
	}
	
	private static void initTaggerDir() {
		taggerDir = new int[n][n];
		taggerRevDir = new int[n][n];
		
		int cx = n / 2;
		int cy = n / 2;
		
		while (true) {
			for (int d = 0; d < 4; d++) {
				for (int cnt = 0; cnt < dc[d]; cnt++) {
					taggerDir[cx][cy] = d;
					
					cx += dx[d];
					cy += dy[d];
					
					// 역방향은 현재 칸 다음에서 반대 방향을 가짐
					taggerRevDir[cx][cy] = (d + 2) % 4; // // 반대 방향 저장
					
					// 이동하는 도중 (0, 0)에 도착하면 종료
					if (cx == 0 && cy == 0) {
						return;
					}
				}
			}
			
			// 이동하는 칸 늘림
			for (int i = 0; i < 4; i++) {
				dc[i] += 2;
			}
		}
		
		// 코드트리 해설
//		int currX = n / 2, currY = n / 2;
//        int moveDir = 0, moveNum = 1;
//    
//        while(currX > 0 || currY > 0) {
//            // moveNum 만큼 이동합니다.
//            for(int i = 0; i < moveNum; i++) {
//                taggerDir[currX][currY] = moveDir;
//                
//                currX += dx[moveDir];
//                currY += dy[moveDir];
//                
//                taggerRevDir[currX][currY] = (moveDir + 2) % 4;
//    
//                // 이동하는 도중 (0, 0)으로 오게 되면,
//                // 움직이는 것을 종료합니다.
//                if(currX == 0 && currY == 0) {
//                	break;
//                }
//            }
//            
//            // 방향을 바꿉니다.
//            moveDir = (moveDir + 1) % 4;
//            // 만약 현재 방향이 위 혹은 아래가 된 경우에는
//            // 특정 방향으로 움직여야 할 횟수를 1 증가시킵니다.
//            if(moveDir == 0 || moveDir == 2) {
//            	moveNum++;
//            }  
//        }
	}
	
	// 모든 도망자 이동
	private static void moveFugitive() {
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				newMap[i][j] = new ArrayList<>();
			}
		}
		
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				int diff = Math.abs(i - sx) + Math.abs(j - sy);
				
				if (diff <= 3) {
					// 해당 칸에 있는 도망자들이 자신의 방향으로 한 칸 이동
					for (int d : map[i][j]) {
						move(i, j, d);
					}
				} else {
					// 거리가 3을 초과하는 경우 움직이지 않음
					newMap[i][j].addAll(map[i][j]);
				}
			}
		}
		
		// 원본 맵에 복사
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				map[i][j] = new ArrayList<>(newMap[i][j]);
			}
		}
	}
	
	// 도망자 이동
	private static void move(int x, int y, int d) {
		int nx = x + dx[d];
		int ny = y + dy[d];
		
		// 격자를 벗어나는 경우 반대 방향으로 바꿔줌
		if (!isRange(nx, ny)) {
			d = (d + 2) % 4;
			nx = x + dx[d];
			ny = y + dy[d];
		}
		
		if (nx == sx && ny == sy) {
			newMap[x][y].add(d); // 술래가 있는 경우 원래 위치
		} else {
			newMap[nx][ny].add(d); // 술래가 없으면 이동한 칸에 방향 저장
		}
	}
	
	// 술래 이동
	private static void moveTagger() {
		int dir = getTaggerDir(); // 술래가 바라보는 방향
		
		sx += dx[dir];
		sy += dy[dir];
		
		// 이동한 칸이 (0, 0)이거나 (n / 2, n / 2)인지 확인해야 함
		
		// 정방향인데 (0, 0)에 도착한 경우 역방향으로 바꿈
		if (sx == 0 && sy == 0 && !reverse) {
			reverse = true;
		}
		
		// 역방향인데 (n / 2, n / 2)에 도착한 경우 정방향으로 바꿈
		if (sx == n / 2 && sy == n / 2 && reverse) {
			reverse = false;
		}
	}
	
	private static int getTaggerDir() {
		return reverse ? taggerRevDir[sx][sy] : taggerDir[sx][sy];
	}
	
	// 도망자 잡기
	private static void catchFugitive(int t) {
		int nx = sx;
		int ny = sy;
		
		int dir = getTaggerDir(); // 술래가 바라보는 방향
		
		// 술래가 위치한 칸부터 3칸 확인
		for (int i = 0; i < 3; i++) {
//			int nx = sx + i * dx[dir];
//          int ny = sy + i * dy[dir];
			
			// 격자를 벗어나지 않고 나무가 없는 칸인 경우
			if (isRange(nx, ny) && !tree[nx][ny]) {
				answer += t * map[nx][ny].size(); // 잡힌 도망자의 수 더함
				map[nx][ny].clear(); // 빈 칸으로 만듦
			}

			nx += dx[dir];
			ny += dy[dir];
		}
	}
	
	private static boolean isRange(int x, int y) {
		return x >= 0 && x < n && y >= 0 && y < n;
	}

}
