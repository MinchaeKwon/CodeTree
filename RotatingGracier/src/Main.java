/**
 * 회전하는 빙하
 * 삼성 SW 역량테스트 2020 하반기 오후 2번 문제
 * https://www.codetree.ai/training-field/frequent-problems/problems/rotating-glacier/description?page=2&pageSize=20
 * 
 * @author minchae
 * @date 2023. 10. 17.
 * 
 * 백준 20058 마법사 상어와 파이어스톰
 * https://www.acmicpc.net/problem/20058
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

public class Main {
	
	// 우하상좌
	static int[] dx = {0, 1, -1, 0};
    static int[] dy = {1, 0, 0, -1};
	
	static int n, q;
	static int[][] map;
	
	static int iceTotal = 0;
	static int iceSize = 0;

	public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        
        n = Integer.parseInt(st.nextToken());
        q = Integer.parseInt(st.nextToken());
        
        n = (int) Math.pow(2, n);
        
        map = new int[n][n];
        
        for (int i = 0; i < n; i++) {
        	st = new StringTokenizer(br.readLine());
        	
        	for (int j = 0; j < n; j++) {
        		map[i][j] = Integer.parseInt(st.nextToken());
        	}
        }
        
        st = new StringTokenizer(br.readLine());
        
        while (q-- > 0) {
        	int l = Integer.parseInt(st.nextToken());
        	
        	if (l > 0) {
        		map = rotate(l);
        	}
        	
        	map = meltingIce();	
        }
        
        findMaxSize();
        
        System.out.println(iceTotal);
        System.out.println(iceSize);
	}
	
	// 1. 격자 나누고 나눠진 격자 안의 숫자들을 90도 회전시키기
//	private static int[][] rotate(int l) {
//		int[][] rotateMap = new int[n][n];
//		
//		l = (int) Math.pow(2, l);
//		
//		// l 크기만큼 격자 나누기
//		for (int x = 0; x < n; x += l) {
//			for (int y = 0; y < n; y += l) {
//				
//				// 나눠진 격자 안의 숫자들을 90도 회전시키기
//				for (int i = 0; i < l; i++) {
//					for (int j = 0; j < l; j++) {
//						rotateMap[x + i][y + j] = map[x + l - 1 - j][y + i];
//					}
//				}
//			}
//		}
//		
//		return rotateMap;
//	}
	
	private static int[][] rotate(int l) {
		int[][] rotateMap = new int[n][n];

		int half = (int) Math.pow(2, l - 1); // 회전시킬 칸의 크기

		l = (int) Math.pow(2, l);

		// 회전할 2^L * 2^L 크기 격자의 왼쪽 위 모서리 위치를 잡음
		for (int i = 0; i < n; i += l)
			for (int j = 0; j < n; j += l) {
				// 움직여야하는 2^(L - 1) * 2^(L - 1) 크기 격자의 왼쪽 위 모서리를 각각 잡아 알맞은 방향으로 이동시킴
				move(i, j, half, 0, rotateMap);
				move(i, j + half, half, 1, rotateMap);
				move(i + half, j, half, 2, rotateMap);
				move(i + half, j + half, half, 3, rotateMap);
			}

		return rotateMap;
	}

	private static void move(int startRow, int startCol, int half, int moveDir, int[][] rotateMap) {
		for (int row = startRow; row < startRow + half; row++)
			for (int col = startCol; col < startCol + half; col++) {
				int nextRow = row + dx[moveDir] * half;
				int nextCol = col + dy[moveDir] * half;

				rotateMap[nextRow][nextCol] = map[row][col];
			}
	}
	
	// 2. 빙하 속의 얼음 녹이기
	private static int[][] meltingIce() {
		int[][] copy = new int[n][n];
		
		for (int i = 0; i < n; i++) {
			copy[i] = Arrays.copyOf(map[i], n);
		}
		
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				int cnt = 0;
				
				// 해당 칸에 얼음이 없으면 다음 칸 탐색
				if (map[i][j] == 0) {
					continue;
				}
				
				// 상하좌우 탐색
				for (int d = 0; d < 4; d++) {
					int nx = i + dx[d];
					int ny = j + dy[d];
					
					if (isRange(nx, ny) && map[nx][ny] > 0) {
						cnt++;
					}
				}
				
				// 인접한 칸에 얼음의 개수가 3개 미만인 경우 해당 칸의 얼음이 녹음
				if (cnt < 3) {
					copy[i][j] = map[i][j] - 1;
				}
			}
		}
		
		return copy;
	}
	
	// 가장 큰 얼음 군집 크기 구하기
	private static void findMaxSize() {
		Queue<int[]> q = new LinkedList<>();
		boolean[][] visited = new boolean[n][n];
		
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				iceTotal += map[i][j];
				
				if (map[i][j] > 0 && !visited[i][j]) {
					q.add(new int[] {i, j});
					visited[i][j] = true;
					
					int size = 1; // 빙하 덩어리 크기
					
					while (!q.isEmpty()) {
						int[] cur = q.poll();
						
						for (int d = 0; d < 4; d++) {
							int nx = cur[0] + dx[d];
							int ny = cur[1] + dy[d];
							
							if (isRange(nx, ny) && !visited[nx][ny] && map[nx][ny] > 0) {
								q.add(new int[] {nx, ny});
								visited[nx][ny] = true;
								
								size++;
							}
						}
					}
					
					iceSize = Math.max(iceSize, size);
				}
			}
		}
	}
	
	private static boolean isRange(int x, int y) {
		return x >= 0 && x < n && y >= 0 && y < n;
	}

}
