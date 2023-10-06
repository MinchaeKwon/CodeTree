/**
 * 자율주행 전기차
 * 삼성 SW 역량테스트 2020 상반기 오후 2번 문제
 * https://www.codetree.ai/training-field/frequent-problems/problems/autonomous-electric-car/description?page=2&pageSize=20
 * 
 * @author minchae
 * @date 2023. 10. 6.
 * 
 * 백준 19238 스타트 택시
 * https://www.acmicpc.net/problem/19238
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

class Point implements Comparable<Point> {
	int x;
	int y;
	int dist; // 거리
	
	public Point(int x, int y, int dist) {
		this.x = x;
		this.y = y;
		this.dist = dist;
	}
	
	@Override
    public int compareTo(Point o) {
		if (this.dist == o.dist) {
			if (this.x == o.x) {
				return this.y - o.y; // 행의 값이 같은 경우 열을 기준으로 오름차순 정렬
			}
			
			return this.x - o.x; // 거리가 같을 경우 행을 기준으로 오름차순 정렬
		}
		
		return this.dist - o.dist; // 거리를 기준으로 오름차순 정렬
	}
}

public class Main {
	
	// 상하좌우
	static int[] dx = {-1, 1, 0, 0};
	static int[] dy = {0, 0, -1, 1};
	
	static int n, m, c;
	
	static int[][] map; // 승객, 빈 칸, 도로 위치 저장 (0 도로, -1 벽, 그 외 승객 번호)
	
	static int sx, sy; // 전기차의 초기 위치
	static Point[] passenger;
	static Point[] destination;

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		n = Integer.parseInt(st.nextToken());
		m = Integer.parseInt(st.nextToken());
		c = Integer.parseInt(st.nextToken());
		
		map = new int[n][n];
		passenger = new Point[m + 1];
		destination = new Point[m + 1];
		
		for (int i = 0; i < n; i++) {
			st = new StringTokenizer(br.readLine());
			
			for (int j = 0; j < n; j++) {
				map[i][j] = Integer.parseInt(st.nextToken());
				
				// 승객 번호 저장하기 위해 벽인 경우 -1로 바꿔줌
				if (map[i][j] == 1) {
					map[i][j] = -1;
				}
			}
		}
		
		// 전기차의 초기 위치
		st = new StringTokenizer(br.readLine());
		sx = Integer.parseInt(st.nextToken()) - 1;
		sy = Integer.parseInt(st.nextToken()) - 1;
		
		for (int i = 1; i <= m; i++) {
			st = new StringTokenizer(br.readLine());
			
			int px = Integer.parseInt(st.nextToken()) - 1;
			int py = Integer.parseInt(st.nextToken()) - 1;
			
			passenger[i] = new Point(px, py, 0);
			map[px][py] = i; // 승객 위치 맵에 저장
			
			int ex = Integer.parseInt(st.nextToken()) - 1;
			int ey = Integer.parseInt(st.nextToken()) - 1;
			
			destination[i] = new Point(ex, ey, 0);
		}
		
		// 승객의 수만큼 반복
		for (int i = 0; i < m; i++) {
			int pos = getPassenger();
			
			// 연료가 부족해 승객을 태울 수 없는 경우 -1 출력
			if (pos == -1) {
				System.out.println(-1);
				return;
			}
			
			// 승객 위치로 전기차 위치 갱신
			sx = passenger[pos].x;
			sy = passenger[pos].y;
			
			if (goToArrival(destination[pos])) {
				// 목적지 위치로 전기차 위치 갱신
				sx = destination[pos].x;
				sy = destination[pos].y;
			} else {
				// 연료가 부족해 목적지까지 갈 수 없는 경우 -1 출력
				System.out.println(-1);
				return;
			}
		}
		
		System.out.println(c); // 남은 연료 출력

	}
	
	// 1. 태울 승객 번호 찾기
	private static int getPassenger() {
		PriorityQueue<Point> pq = new PriorityQueue<>();
		boolean[][] visited = new boolean[n][n];
		
		pq.add(new Point(sx, sy, 0));
		visited[sx][sy] = true;
		
		while (!pq.isEmpty()) {
			Point cur = pq.poll();
			
			// 승객을 발견한 경우
			if (map[cur.x][cur.y] > 0) {
				c -= cur.dist;
				
				int num = map[cur.x][cur.y];
				map[cur.x][cur.y] = 0; // 승객 태웠으니까 0으로 변경
				
				return c >= 0 ? num : -1; // 연료가 부족한 경우에는 -1 반환
			}
			
			for (int i = 0; i < 4; i++) {
				int nx = cur.x + dx[i];
				int ny = cur.y + dy[i];
				
				// 범위를 벗어나지 않고, 아직 방문하지 않았으며 벽이 아닌 경우
				if (isRange(nx, ny) && !visited[nx][ny] && map[nx][ny] != -1) {
					pq.add(new Point(nx, ny, cur.dist + 1));
					visited[nx][ny] = true;
				}
			}
		}
		
		return -1;
	}
	
	// 2. 목적지까지 이동
	private static boolean goToArrival(Point arrival) {
		PriorityQueue<Point> pq = new PriorityQueue<>();
		boolean[][] visited = new boolean[n][n];
		
		pq.add(new Point(sx, sy, 0));
		visited[sx][sy] = true;
		
		while (!pq.isEmpty()) {
			Point cur = pq.poll();
			
			if (cur.x == arrival.x && cur.y == arrival.y) {
				c -= cur.dist;
				
				if (c < 0) {
					return false;
				}
				
				c += cur.dist * 2; // 소모한 배터리 양의 2배 충전
				return true;
			}
			
			for (int i = 0; i < 4; i++) {
				int nx = cur.x + dx[i];
				int ny = cur.y + dy[i];
				
				if (isRange(nx, ny) && !visited[nx][ny] && map[nx][ny] != -1) {
					pq.add(new Point(nx, ny, cur.dist + 1));
					visited[nx][ny] = true;
				}
			}
		}
		
		return false;
	}
	
	private static boolean isRange(int x, int y) {
		return x >= 0 && x < n && y >= 0 && y < n;
	}

}
