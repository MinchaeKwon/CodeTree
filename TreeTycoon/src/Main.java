/**
 * 나무 타이쿤
 * 삼성 SW 역량테스트 2021 상반기 오후 1번 문제
 * https://www.codetree.ai/training-field/frequent-problems/problems/tree-tycoon/description?page=2&pageSize=20
 * 
 * @author minchae
 * @date 2023. 10. 16.
 * 
 * 백준 21610 마법사 상어와 비바라기
 * https://www.acmicpc.net/problem/21610
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

public class Main {
	
	// → ↗ ↑ ↖ ← ↙ ↓ ↘
	static int[] dx = {0, -1, -1, -1, 0, 1, 1, 1};
	static int[] dy = {1, 1, 0, -1, -1, -1, 0, 1};
	
	static int n, m;
	
	static int[][] map;
	static Queue<int[]> q = new LinkedList<>();
	static boolean[][] visited;
	
	public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        
        map = new int[n][n];
        
        for (int i = 0; i < n; i++) {
        	st = new StringTokenizer(br.readLine());
        	
        	for (int j = 0; j < n; j++) {
        		map[i][j] = Integer.parseInt(st.nextToken());
        	}
        }
        
        // 초기 특수 영양제 위치 추가
        q.add(new int[] {n - 1, 0});
        q.add(new int[] {n - 1, 1});
        q.add(new int[] {n - 2, 0});
        q.add(new int[] {n - 2, 1});
        
        while (m-- > 0) {
        	st = new StringTokenizer(br.readLine());
        	
        	int d = Integer.parseInt(st.nextToken()) - 1; // 방향
        	int p = Integer.parseInt(st.nextToken()); // 이동 칸 수
        	
        	visited = new boolean[n][n];
        	
        	move(d, p);
        	grow();
        	buyTonic();
        }
        
        int result = 0;
        
        for (int i = 0; i < n; i++) {
        	for (int j = 0; j < n; j++) {
        		result += map[i][j];
        	}
        }
        
        System.out.println(result);
	}
	
	// 특수 영양제 이동시키고 해당 땅에 특수 영양제 투입
	private static void move(int d, int p) {
		for (int[] tonic : q) {
			// d방향으로 p칸 이동
			int nx = (tonic[0] + dx[d] * p + p * n) % n;
			int ny = (tonic[1] + dy[d] * p + p * n) % n;
			
			tonic[0] = nx;
			tonic[1] = ny;
			
			map[nx][ny]++;
			visited[nx][ny] = true; // 특수 영양제가 투입된 리브로수는 제외해야 하기 때문에 visited 배열 방문처리 해줌
		}
	}
	
	/*
	 * 특수 영양제를 투입한 리브로수의 대각선으로 인접한 방향에 높이가 1 이상인 리브로수가 있는 만큼 높이가 더 성장 
	 * 대각선으로 인접한 방향이 격자를 벗어나는 경우에는 세지 않음
	 * */
	private static void grow() {
		while (!q.isEmpty()) {
			int[] cur = q.poll();
			
			int x = cur[0];
			int y = cur[1];
			
			int cnt = 0;
			
			// 대각선 방향 확인
			for (int i = 1; i < 8; i += 2) {
				int nx = x + dx[i];
				int ny = y + dy[i];
				
				if (isRange(nx, ny) && map[nx][ny] >= 1) {
					cnt++;
				}
			}
			
			map[x][y] += cnt;
		}
	}
	
	/*
	 * 특수 영양제를 투입한 리브로수를 제외하고 높이가 2 이상인 리브로수는 높이 2를 베어서 잘라낸 리브로수로 특수 영양제를 사고, 해당 위치에 특수 영양제를 올려둠
	 * */
	private static void buyTonic() {
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				// 특수 영양제를 투입한 리브로수가 아니고, 높이가 2이상인 경우
				if (!visited[i][j] && map[i][j] >= 2) {
					map[i][j] -= 2; // 높이 2를 벰
					q.add(new int[] {i, j}); // 특수 영양제 사기
				}
			}
		}
	}
	
	private static boolean isRange(int x, int y) {
		return x >= 0 && x < n && y >= 0 && y < n;
	}
	
}
