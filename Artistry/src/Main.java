/**
 * 예술성
 * 삼성 SW 역량테스트 2022 상반기 오전 2번 문제
 * https://www.codetree.ai/training-field/frequent-problems/problems/artistry/description?page=1&pageSize=20
 * 
 * @author minchae
 * @date 2023. 10. 20.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

public class Main {
	
	// 상하좌우
	static int[] dx = {-1, 1, 0, 0};
	static int[] dy = {0, 0, -1, 1};
	
	static int n;
	
	static int[][] map;
	static int[][] newMap;
	
	static Queue<int[]> q;
	static boolean[][] visited;
	
	static int groupNum;
	static int[] groupCnt; // 각 그룹의 칸 개수 저장
	static int[][] group; // 그룹 번호 저장

	public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        
        n = Integer.parseInt(br.readLine());
        
        map = new int[n][n];
        groupCnt = new int[n * n + 1];
        group = new int[n][n];
        
        for (int i = 0; i < n; i++) {
        	StringTokenizer st = new StringTokenizer(br.readLine());
        	
        	for (int j = 0; j < n; j++) {
        		map[i][j] = Integer.parseInt(st.nextToken());
        	}
        }
        
        int result = 0;
        
        for (int i = 0; i < 4; i++) {
        	makeGroup();
        	result += getScore();
        	rotate();
        }
        
        System.out.println(result);
	}
	
	// 그룹 만들기
	private static void makeGroup() {
		q = new LinkedList<>();
		visited = new boolean[n][n];

		groupNum = 0;

		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (!visited[i][j]) {
					bfs(i, j);
				}
			}
		}
	}

	// bfs를 통해 각 그룹 구하기
	private static void bfs(int x, int y) {
		q.add(new int[] { x, y });
		visited[x][y] = true;

		groupNum++;

		group[x][y] = groupNum;
		groupCnt[groupNum] = 1;

		while (!q.isEmpty()) {
			int[] cur = q.poll();

			for (int i = 0; i < 4; i++) {
				int nx = cur[0] + dx[i];
				int ny = cur[1] + dy[i];

				// 범위 벗어나지 않음, 방문하지 않음, 같은 그룹인 경우
				if (checkRange(nx, ny) && !visited[nx][ny] && map[nx][ny] == map[x][y]) {
					q.add(new int[] { nx, ny });
					visited[nx][ny] = true;

					group[nx][ny] = groupNum;
					groupCnt[groupNum]++;
				}
			}
		}
	}
	
	// 예술 점수 구하기
	private static int getScore() {
		int score = 0;
		
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				for (int d = 0; d < 4; d++) {
					int nx = i + dx[d];
					int ny = j + dy[d];
					
					// 범위를 벗어나지 않고 그룹이 서로 다른 경우 (특정 변을 두고 두 칸의 그룹이 다른 경우)
					if (checkRange(nx, ny) && map[i][j] != map[nx][ny]) {
						// 그룹을 이루고 있는 숫자 값
						int n1 = map[i][j];
						int n2 = map[nx][ny];
						
						// 그룹 번호
						int g1 = group[i][j];
						int g2 = group[nx][ny];
						
						// 그룹에 속한 칸의 수
						int cnt1 = groupCnt[g1];
						int cnt2 = groupCnt[g2];
						
						score += (cnt1 + cnt2) * n1 * n2;
					}
				}
			}
		}
		
		return score /= 2; // 중복값 제외 (예를 들어 (g1, g2), (g2, g1) 이렇게 중복으로 계산이 들어가기 때문에 나누기 2를 해줌)
	}
	
	// 회전시키기
	private static void rotate() {
		newMap = new int[n][n];
		
		// 십자모양 반시계 방향으로 90도 회전
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (i == n / 2 || j == n / 2) {
					newMap[n - j - 1][i] = map[i][j];
				}
				
//                if (j == n / 2) {
//                	newMap[j][i] = map[i][j]; // 세로줄인 경우 (i, j) -> (j, i)
//                } else if (i == n / 2) {
//                	newMap[n - j - 1][i] = map[i][j]; // 가로줄인 경우 (i, j) -> (n - j - 1, i)가 됨
//                }
			}
		}
		
		int size = n / 2;
		
		rotateSquare(0, 0, size);
		rotateSquare(0, size + 1, size);
		rotateSquare(size + 1, 0, size);
		rotateSquare(size + 1, size + 1, size);
		
		map = newMap;
	}
	
	// 4개의 사각형 시계 방향으로 90도 회전
	private static void rotateSquare(int sx, int sy, int size) {
//		for (int i = sx; i < sx + size; i++) {
//			for (int j = sy; j < sy + size; j++) {
//				// Step 1. (sx, sy)를 (0, 0)으로 옮겨주는 변환을 진행
//                int ox = i - sx;
//                int oy = j - sy;
//                
//                // Step 2. 변환된 상태에서는 회전 이후의 좌표가 (x, y) -> (y, squareN - x - 1)가 됨
//                int rx = oy;
//                int ry = size - ox - 1;
//                
//                // Step 3. 다시 (sx, sy)를 더해줌
//                newMap[rx + sx][ry + sy] = map[i][j];
//			}
//		}
		
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				newMap[sy + j][sx + size - i - 1] = map[sy + i][sx + j];
			}
		}
	}
	
	private static boolean checkRange(int x, int y) {
		return x >= 0 && x < n && y >= 0 && y < n;
	}

}
