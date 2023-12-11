/**
 * 예술성
 * 
 * @author minchae
 * @date 2023. 12. 11.
 */

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
	static int[][] map; // 각 칸에 칠해져 있는 색깔에 대한 정보
	
	static Queue<Pair> q;
	static boolean[][] visited;
	
	static int groupN; // 그룹 번호
	static int[] cnt; // 각 그룹마다 몇 개의 칸이 있는지 저장
	static int[][] group; // 각 칸의 그룹 번호 저장
	
	static int[][] newMap;

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
		
		for (int i = 0; i <= 3; i++) {
			makeGroup();
			answer += getScore();
			rotate();
		}

		System.out.println(answer);
	}
	
	// 그룹 만들기
	private static void makeGroup() {
		q = new LinkedList<>();
		visited = new boolean[n][n];
		
		groupN = 0;
		cnt = new int[n * n + 1];
		group = new int[n][n];
		
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (!visited[i][j]) {
					bfs(i, j);
				}
			}
		}
	}
	
	private static void bfs(int x, int y) {
		q.add(new Pair(x, y));
		visited[x][y] = true;
		
		groupN++;
		cnt[groupN] = 1;
		group[x][y] = groupN;
		
		while (!q.isEmpty()) {
			Pair cur = q.poll();
			
			for (int i = 0; i < 4; i++) {
				int nx = cur.x + dx[i];
				int ny = cur.y + dy[i];
				
				// 아직 방문하지 않았고, 같은 색깔인 경우
				if (isRange(nx, ny) && !visited[nx][ny] && map[nx][ny] == map[x][y]) {
					q.add(new Pair(nx, ny));
					visited[nx][ny] = true;
					
					cnt[groupN]++;
					group[nx][ny] = groupN;
				}
			}
		}
	}
	
	// 예술 점수 구하기
	private static int getScore() {
		// (그룹 a에 속한 칸의 수 + 그룹 b에 속한 칸의 수 ) x 그룹 a를 이루고 있는 숫자 값 x 그룹 b를 이루고 있는 숫자 값 x 그룹 a와 그룹 b가 서로 맞닿아 있는 변의 수
		
		int score = 0;
		
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				for (int d = 0; d < 4; d++) {
					int nx = i + dx[d];
					int ny = j + dy[d];
					
					if (isRange(nx, ny) && map[nx][ny] != map[i][j]) {
						// 그룹 번호
						int g1 = group[i][j];
						int g2 = group[nx][ny];
						
						// 그룹에 속한 칸의 수
						int cnt1 = cnt[g1];
						int cnt2 = cnt[g2];
						
						// 그룹을 이루고 있는 숫자 값
						int n1 = map[i][j];
						int n2 = map[nx][ny];
						
						score += (cnt1 + cnt2) * n1 * n2;
					}
				}
			}
		}
		
		return score / 2;
	}
	
	// 회전시키기
	private static void rotate() {
		newMap = new int[n][n];
		
		// 십자 모양의 경우 통째로 반시계 방향으로 90' 회전
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (i == n / 2 || j == n / 2) {
					newMap[n - j - 1][i] = map[i][j];
				}
			}
		}
		
		int size = n / 2;
		
		rotateSquare(0, 0, size);
		rotateSquare(0, size + 1, size);
		rotateSquare(size + 1, 0, size);
		rotateSquare(size + 1, size + 1, size);
		
		map = newMap;
	}
	
	// 십자 모양을 제외한 4개의 정사각형 시계 방향으로 90'씩 회전
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
