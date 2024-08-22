/**
 * 코드트리 투어
 * https://www.codetree.ai/training-field/frequent-problems/problems/codetree-tour/description?page=1&pageSize=5
 * 
 * @author minchae
 * @date 2024. 8. 23.
 */

import java.util.*;
import java.io.*;

// 모든 상품이 연결되어 있지 않기 때문에 다익스트라 사용 (한 정점에서 다른 모든 정점까지의 거리 계산)
public class Main {
	
	static final int INF = Integer.MAX_VALUE;
	static final int MAX_ID = 30001;
	
	// 상품의 정보 저장
	static class Node implements Comparable<Node> {
		int e;
		int w;
		int cost; // 출발지에서 도착지까지의 거리
		
		public Node(int e, int w) {
			this.e = e;
			this.w = w;
		}

		@Override
		public int compareTo(Main.Node o) {
			return this.w - o.e;
		}
	}
	
	static class Product implements Comparable<Product> {
		int id;
		int revenue;
		int dest;
		int profit;
		
		public Product(int id, int revenue, int dest, int profit) {
			this.id = id;
			this.revenue = revenue;
			this.dest = dest;
			this.profit = profit;
		}

		@Override
		public int compareTo(Main.Product o) {
			if (this.profit == o.profit) {
				return Integer.compare(this.id, o.id);
			}
			
			return Integer.compare(o.profit, this.profit);
		}
	}
	
	static ArrayList<Node>[] list;
	static int[] dist;
	
	static PriorityQueue<Product> products = new PriorityQueue<>();
	
	// 우선순위 큐에서는 바로 해당 상품을 삭제할 수 없어서 따로 배열을 만듦
	static boolean[] isExist;
	static boolean[] isDelete;
	
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		int Q = Integer.parseInt(br.readLine());
		
		while (Q-- > 0) {
			StringTokenizer st = new StringTokenizer(br.readLine());
			
			int num = Integer.parseInt(st.nextToken());
			
			switch (num) {
			case 100:
				// 코드트리 랜드 건설 -> 그래프 초기화
				int n = Integer.parseInt(st.nextToken());
				int m = Integer.parseInt(st.nextToken());
				
				list = new ArrayList[n];
				dist = new int[n];
				isExist = new boolean[30001];
				isDelete = new boolean[30001];
				
				for (int i = 0; i < n; i++) {
					list[i] = new ArrayList<>();
				}
				
				for (int i = 0; i < m; i++) {
					int v = Integer.parseInt(st.nextToken()); // 출발지
					int u = Integer.parseInt(st.nextToken()); // 도착지
					int w = Integer.parseInt(st.nextToken()); // 가중치
					
					// 무방향이므로 양쪽 다 추가
					list[v].add(new Node(u, w)); // 그래프 만듦
					list[u].add(new Node(v, w));
				}
				
				// 초기에 0번부터 모든 정점까지의 거리 구함
				dijkstra(0);
				
				break;
			case 200:
				int id = Integer.parseInt(st.nextToken());
				int revenue = Integer.parseInt(st.nextToken());
				int dest = Integer.parseInt(st.nextToken());
				
				create(id, revenue, dest);
				
				break;
			case 300:
				id = Integer.parseInt(st.nextToken());
				delete(id);
				
				break;
			case 400:
				System.out.println(sell());
				break;
			case 500:
				// 여행 상품의 출발지 변경
				int s = Integer.parseInt(st.nextToken());
				update(s);
				
				break;
			}
		}
	}
	
	// 여행 상품 생성
	private static void create(int id, int revenue, int dest) {
		isExist[id] = true;
		
		int profit = revenue - dist[dest];
		products.add(new Product(id, revenue, dest, profit)); // 상품 추가
	}
	
	// 여행 상품 취소 -> 관리 목록에서 삭제
	private static void delete(int id) {
		if (isExist[id]) {
			isDelete[id] = true;
		}
	}
	
	// 최적의 여행 상품 판매
	private static int sell() {
		// 판매 불가 상품은 제외함
		while (!products.isEmpty()) {
			Product cur = products.peek();
			
			// 우선 순위 제일 높은 걸 꺼냈을 때 판매 불가면 그 뒤에 있는 상품도 판매하지 못함
			if (cur.profit < 0) {
				return -1;
			}
			
			products.poll();
			
			if (!isDelete[cur.id]) {
				return cur.id;
			}
		}
		
		return -1;
	}
	
	private static void update(int s) {
		dijkstra(s); // s부터 모든 정점까지의 거리 초기화 -> 거리값 변경됨
		
		ArrayList<Product> tmp = new ArrayList<>(products);
		
		products.clear();
		
		// 다시 추가하면서 수익 재조정
		for (Product p : tmp) {
			create(p.id, p.revenue, p.dest);
		}
	}
	
	// 특정 정점에서 모든 정점까지의 거리 구하기
	private static void dijkstra(int start) {
		PriorityQueue<Node> pq = new PriorityQueue<>();
		
		Arrays.fill(dist, INF);
		dist[start] = 0;
		
		pq.add(new Node(start, 0));
		
		while (!pq.isEmpty()) {
			Node cur = pq.poll();
			
			if (dist[cur.e] < cur.w) {
				continue;
			}
			
			for (Node next : list[cur.e]) {
				if (dist[next.e] > dist[cur.e] + next.w) {
					dist[next.e] = dist[cur.e] + next.w;
					pq.add(new Node(next.e, dist[next.e]));
				}
			}
		}
	}

}
