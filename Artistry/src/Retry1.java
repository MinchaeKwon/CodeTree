/**
 * 예술성
 * 
 * @author minchae
 * @date 2024. 3. 27.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

public class Retry1 {
	
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
	
	static boolean[][] visited;
	
	static int groupNum;
	static int[] groupCnt; // 각 그룹의 칸 수 저장
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
        
        int answer = 0;

        for (int i = 0; i < 4; i++) {
        	makeGroup();
        	answer += getScore();
        	rotate();
        }
        
        System.out.println(answer);
	}
	
	private static void makeGroup() {
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
	
	// 그룹 번호, 칸의 개수 구함
	private static void bfs(int x, int y) {
		Queue<Pair> q = new LinkedList<>();
		
		q.add(new Pair(x, y));
		visited[x][y] = true;
		
		groupNum++;
		group[x][y] = groupNum;
		groupCnt[groupNum] = 1;
		
		while (!q.isEmpty()) {
			Pair cur = q.poll();
			
			for (int i = 0; i < 4; i++) {
				int nx = cur.x + dx[i];
				int ny = cur.y + dy[i];
				
				if (!isRange(nx, ny) || visited[nx][ny]) {
					continue;
				}
				
				if (map[x][y] == map[nx][ny]) {
					q.add(new Pair(nx, ny));
					visited[nx][ny] = true;
					
					groupCnt[groupNum]++;
					group[nx][ny] = groupNum;
				}
			}
		}
	}
	
	private static int getScore() {
		int score = 0;
		
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				for (int d = 0; d < 4; d++) {
					int nx = i + dx[d];
					int ny = j + dy[d];
					
					if (isRange(nx, ny) && map[nx][ny] != map[i][j]) {
						int n1 = map[i][j];
						int n2 = map[nx][ny];
						
						int g1 = group[i][j];
						int g2 = group[nx][ny];
						
						int cnt1 = groupCnt[g1];
						int cnt2 = groupCnt[g2];
						
						score += (cnt1 + cnt2) * n1 * n2;
					}
				}
			}
		}
		
		return score / 2;
	}
	
	private static void rotate() {
		newMap = new int[n][n];
		
		int size = n / 2;
		
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
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
