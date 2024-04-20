/**
 * 예술성
 * 
 * @author minchae
 * @date 2024. 4. 21.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;


public class Retry2 {
	
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
	
	static int groupNum;
	static int[][] group;
	static int[] groupCnt;
	
	static boolean[][] visited;
	static int[][] newMap;

	public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        n = Integer.parseInt(br.readLine());

        map = new int[n][n];
        group = new int[n][n];
        groupCnt = new int[n * n + 1];
        
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
					bfs(i, j, map[i][j]);
				}
			}
		}
	}
	
	private static void bfs(int x, int y, int color) {
		Queue<Pair> q = new LinkedList<>();
		
		q.add(new Pair(x, y));
		visited[x][y] = true;
		
		groupNum++;
		group[x][y] = groupNum;
		
		int cnt = 1;
		
		while (!q.isEmpty()) {
			Pair cur = q.poll();
			
			for (int i = 0; i < 4; i++) {
				int nx = cur.x + dx[i];
				int ny = cur.y + dy[i];
				
				if (!isRange(nx, ny) || visited[nx][ny] || map[nx][ny] != color) {
					continue;
				}
				
				q.add(new Pair(nx, ny));
				visited[nx][ny] = true;
				
				group[nx][ny] = groupNum;
				cnt++;
			}
		}
		
		groupCnt[groupNum] = cnt;
	}
	
	private static int getScore() {
		int score = 0;
		
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				for (int d = 0; d < 4; d++) {
					int nx = i + dx[d];
					int ny = j + dy[d];
					
					if (isRange(nx, ny) && map[nx][ny] != map[i][j]) {
						int g1 = group[i][j];
						int g2 = group[nx][ny];
						
						int cnt1 = groupCnt[g1];
						int cnt2 = groupCnt[g2];
						
						int n1 = map[i][j];
						int n2 = map[nx][ny];
						
						score += (cnt1 + cnt2) * n1 * n2;
					}
				}
			}
		}
		
		return score / 2;
	}
	
	private static void rotate() {
		newMap = new int[n][n];
		
		// 십자모양 반시계 90도 회전
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
