/**
 * 예술성
 * 
 * @author minchae
 * @date 2024. 10. 1.
 * 
 * 문제 접근 아이디어 및 알고리즘 판단 사유
 * 		- 그룹 만들기 : bfs 사용
 * 		- 예술 점수 구하는 부분 : 4방 탐색하면서 숫자가 다르면 점수 계산 -> 중복 계산이 될 수 있어서 마지막에 2를 나눠줌
 * 
 * 시간 복잡도
 * 그룹 만들기 : O(N^2)
 * 예술 점수 계산 : O(N^2)
 * 총 시간 복잡도 : 4번 반복하기 때문에 O(4 * N^2)
 * 
 * 실행 시간
 * 115ms
 * */

import java.io.*;
import java.util.*;

public class Artistry {
	
	static class Pair {
		int x;
		int y;
		
		public Pair(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}
	
	// 상하좌우
	static int[] dx = {-1, 1, 0, 0};
	static int[] dy = {0, 0, -1, 1};
	
	static int n;
	
	static int[][] map;
	static int[][] newMap;
	
	static int[][] group; // 그룹 번호 저장
	static int[] groupCnt; // 각 그룹의 블록 개수
	
	static boolean[][] visited;

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		n = Integer.parseInt(br.readLine());
		
		map = new int[n][n];
		
		for (int i = 0; i < n; i++) {
			StringTokenizer st = new StringTokenizer(br.readLine());
			
			for (int j = 0; j < n; j++) {
				map[i][j] = Integer.parseInt(st.nextToken());
			}
		}
		
		int answer = 0;
		
		for (int i = 0; i < 4; i++) {
			makeGroup();
			
			answer += getScore();
			
			rotate();
		}
		
		System.out.println(answer);
	}
	
	// 그룹 만들기
	private static void makeGroup() {
		visited = new boolean[n][n];
		
		group = new int[n][n];
		groupCnt = new int[n * n + 1];
		
		int num = 1;
		
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (!visited[i][j]) {
					bfs(i, j, num++);
				}
			}
		}
	}
	
	private static void bfs(int x, int y, int num) {
		Queue<Pair> q = new LinkedList<>();
		
		q.add(new Pair(x, y));
		visited[x][y] = true;
		
		int cnt = 1;
		group[x][y] = num;
		
		while (!q.isEmpty()) {
			Pair cur = q.poll();
			
			for (int i = 0; i < 4; i++) {
				int nx = cur.x + dx[i];
				int ny = cur.y + dy[i];
				
				if (!isRange(nx, ny) || visited[nx][ny] || map[nx][ny] != map[x][y]) {
					continue;
				}
				
				q.add(new Pair(nx, ny));
				visited[nx][ny] = true;
				
				group[nx][ny] = num;
				cnt++;
			}
		}
		
		groupCnt[num] = cnt;
	}
	
	private static int getScore() {
		int score = 0;
		
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				for (int d = 0; d < 4; d++) {
					int nx = i + dx[d];
					int ny = j + dy[d];
					
					if (!isRange(nx, ny) || map[nx][ny] == map[i][j]) {
						continue;
					}
					
					int g1 = group[i][j];
					int g2 = group[nx][ny];
					
					int c1 = groupCnt[g1];
					int c2 = groupCnt[g2];
					
					int n1 = map[i][j];
					int n2 = map[nx][ny];
					
					score += (c1 + c2) * n1 * n2;
				}
			}
		}
		
		return score / 2;
	}
	
	private static void rotate() {
		newMap = new int[n][n];
		
		// 십자 모양 반시계 방향 회전
		int size = n / 2;
		
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				// 중간 행이나 중간 열인 경우
				if (i == size || j == size) {
					newMap[n - j - 1][i] = map[i][j];
				}
			}
		}
		
		rotateSquare(0, 0, size);
		rotateSquare(0, size + 1, size);
		rotateSquare(size + 1, 0, size);
		rotateSquare(size + 1, size + 1, size);
		
		map = newMap;
	}
	
	// 시계 방향으로 회전
	private static void rotateSquare(int sx, int sy, int size) {
		for (int i = sx; i < sx + size; i++) {
			for (int j = sy; j < sy + size; j++) {
				int ox = i - sx;
				int oy = j - sy;
				
				int rx = oy;
				int ry = size - ox - 1;
				
				newMap[rx + sx][ry + sy] = map[i][j];
			}
		}
	}

	private static boolean isRange(int x, int y) {
		return x >= 0 && x < n && y >= 0 && y < n;
	}
}
