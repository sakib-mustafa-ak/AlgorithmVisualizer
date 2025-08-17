package com.algovis.pseudo;

import com.algovis.core.AlgorithmId;

public final class PseudocodeRepository {

    public static String cpp(AlgorithmId id){
        return switch (id) {
            case BUBBLE_SORT -> """
// C++ — Bubble Sort
void bubbleSort(vector<int>& a){
    bool swapped=true;
    for (int n=a.size(); swapped && n>1; --n){
        swapped=false;
        for (int i=1;i<n;i++){
            if (a[i-1] > a[i]) {
                swap(a[i-1], a[i]);
                swapped=true;
            }
        }
    }
}
""";
            case SELECTION_SORT -> """
// C++ — Selection Sort
void selectionSort(vector<int>& a) {
    int n = a.size();
    for (int i = 0; i < n - 1; i++) {
        int min_idx = i;
        for (int j = i + 1; j < n; j++) {
            if (a[j] < a[min_idx])
                min_idx = j;
        }
        swap(a[min_idx], a[i]);
    }
}
""";
            case INSERTION_SORT -> """
// C++ — Insertion Sort
void insertionSort(vector<int>& a) {
    int n = a.size();
    for (int i = 1; i < n; i++) {
        int key = a[i];
        int j = i - 1;
        while (j >= 0 && a[j] > key) {
            a[j + 1] = a[j];
            j = j - 1;
        }
        a[j + 1] = key;
    }
}
""";
            case BFS -> """
// C++ — BFS
vector<int> bfs(const vector<vector<int>>& adj, int s){
    vector<int> dist(adj.size(), -1);
    queue<int> q;
    dist[s]=0;
    q.push(s);
    while (!q.empty()){
        int u=q.front(); q.pop();
        for(int v: adj[u]) {
            if (dist[v]==-1){
                dist[v]=dist[u]+1;
                q.push(v);
            }
        }
    }
    return dist;
}
""";
            case DFS -> """
// C++ — DFS (recursive)
void dfsUtil(int u, const vector<vector<int>>& adj, vector<int>& vis){
    vis[u]=1;
    for(int v: adj[u]){
        if(!vis[v]) dfsUtil(v, adj, vis);
    }
}
void dfs(const vector<vector<int>>& adj, int s){
    vector<int> vis(adj.size(), 0);
    dfsUtil(s, adj, vis);
}
""";
            case DIJKSTRA -> """
// C++ — Dijkstra's Algorithm
void dijkstra(const vector<vector<pair<int, int>>>& adj, int s) {
    vector<long long> dist(adj.size(), -1);
    priority_queue<pair<long long, int>,
                   vector<pair<long long, int>>,
                   greater<pair<long long, int>>> pq;
    dist[s] = 0;
    pq.push({0, s});
    while (!pq.empty()) {
        auto [d, u] = pq.top();
        pq.pop();
        if (d > dist[u]) continue;
        for (auto& edge : adj[u]) {
            int v = edge.first;
            int w = edge.second;
            if (dist[u] + w < dist[v]) {
                dist[v] = dist[u] + w;
                pq.push({dist[v], v});
            }
        }
    }
}
""";
            case ASTAR -> """
// C++ — A* Search (heuristic provided externally)
struct Node { int v; double f; };
struct Cmp { bool operator()(const Node& a, const Node& b) const { return a.f > b.f; } };

vector<int> a_star(const vector<vector<pair<int,double>>>& adj,
                   int start, int goal,
                   function<double(int)> heuristic){
    int n = adj.size();
    vector<double> g(n, 1e100), f(n, 1e100);
    vector<int> parent(n, -1), vis(n, 0);
    priority_queue<Node, vector<Node>, Cmp> pq;

    g[start] = 0.0;
    f[start] = heuristic(start);
    pq.push({start, f[start]});

    while(!pq.empty()){
        int u = pq.top().v; pq.pop();
        if (vis[u]) continue;
        vis[u] = 1;
        if (u == goal) break;

        for (auto [v, w] : adj[u]){
            double tentative = g[u] + w;
            if (tentative < g[v]){
                g[v] = tentative;
                f[v] = tentative + heuristic(v);
                parent[v] = u;
                pq.push({v, f[v]});
            }
        }
    }
    // reconstruct
    vector<int> path;
    if (parent[goal] == -1 && start != goal) return path;
    for (int cur = goal; cur != -1; cur = parent[cur]) path.push_back(cur);
    reverse(path.begin(), path.end());
    return path;
}
""";
            case KRUSKAL -> """
// C++ — Kruskal's MST
struct Edge { int u,v; int w; };
struct DSU {
    vector<int> p;
    DSU(int n): p(n){ iota(p.begin(), p.end(), 0); }
    int find(int x){ return p[x]==x?x:p[x]=find(p[x]); }
    bool unite(int a,int b){
        a=find(a); b=find(b);
        if(a==b) return false;
        p[a]=b; return true;
    }
};
vector<Edge> kruskal(int n, vector<Edge> edges){
    sort(edges.begin(), edges.end(), [](auto& a, auto& b){return a.w<b.w;});
    DSU dsu(n);
    vector<Edge> mst;
    for(auto &e: edges){
        if(dsu.unite(e.u, e.v)) mst.push_back(e);
        if((int)mst.size()==n-1) break;
    }
    return mst;
}
""";
            case PRIM -> """
// C++ — Prim's MST (min-heap)
vector<pair<int,int>> prim(const vector<vector<pair<int,int>>>& adj, int start){
    int n = adj.size();
    vector<int> in(n,0), parent(n,-1), key(n, INT_MAX);
    using P = pair<int,int>; // (key,node)
    priority_queue<P, vector<P>, greater<P>> pq;

    key[start]=0; pq.push({0,start});
    vector<pair<int,int>> mst;

    while(!pq.empty()){
        auto [_, u] = pq.top(); pq.pop();
        if(in[u]) continue;
        in[u]=1;
        if(parent[u]!=-1) mst.push_back({parent[u], u});
        for(auto [v,w]: adj[u]){
            if(!in[v] && w < key[v]){
                key[v]=w; parent[v]=u;
                pq.push({key[v], v});
            }
        }
    }
    return mst;
}
""";
            case MERGE_SORT -> """
// C++ — Merge Sort (top-down)
void merge(vector<int>& a, int l, int m, int r){
    vector<int> L(a.begin()+l, a.begin()+m+1);
    vector<int> R(a.begin()+m+1, a.begin()+r+1);
    int i=0,j=0,k=l;
    while(i<(int)L.size() && j<(int)R.size()){
        if(L[i] <= R[j]) a[k++]=L[i++];
        else a[k++]=R[j++];
    }
    while(i<(int)L.size()) a[k++]=L[i++];
    while(j<(int)R.size()) a[k++]=R[j++];
}
void mergeSort(vector<int>& a, int l, int r){
    if(l>=r) return;
    int m=(l+r)/2;
    mergeSort(a,l,m);
    mergeSort(a,m+1,r);
    merge(a,l,m,r);
}
""";
            case QUICK_SORT -> """
// C++ — Quick Sort (Lomuto partition)
int partition(vector<int>& a, int l, int r){
    int pivot=a[r], i=l;
    for(int j=l;j<r;j++){
        if(a[j] < pivot){
            swap(a[i], a[j]);
            i++;
        }
    }
    swap(a[i], a[r]);
    return i;
}
void quickSort(vector<int>& a, int l, int r){
    if(l < r){
        int p = partition(a, l, r);
        quickSort(a, l, p-1);
        quickSort(a, p+1, r);
    }
}
""";
            case HEAP_SORT -> """
// C++ — Heap Sort (max-heap)
void heapify(vector<int>& a, int n, int i){
    int largest=i, L=2*i+1, R=2*i+2;
    if(L<n && a[L]>a[largest]) largest=L;
    if(R<n && a[R]>a[largest]) largest=R;
    if(largest!=i){
        swap(a[i], a[largest]);
        heapify(a, n, largest);
    }
}
void heapSort(vector<int>& a){
    int n=a.size();
    for(int i=n/2-1;i>=0;i--) heapify(a, n, i);
    for(int i=n-1;i>0;i--){
        swap(a[0], a[i]);
        heapify(a, i, 0);
    }
}
""";
            case BST_INSERT -> """
// C++ — BST Insert
struct Node{ int key; Node* left; Node* right; Node(int k):key(k),left(NULL),right(NULL){} };
Node* insert(Node* root, int key){
    if(root==NULL) return new Node(key);
    if(key < root->key) root->left = insert(root->left, key);
    else if(key > root->key) root->right = insert(root->right, key);
    return root;
}
""";
            case BST_DELETE -> """
// C++ — BST Delete
int minValue(Node* root){
    while(root->left) root = root->left;
    return root->key;
}
Node* deleteNode(Node* root, int key){
    if(!root) return root;
    if(key < root->key) root->left = deleteNode(root->left, key);
    else if(key > root->key) root->right = deleteNode(root->right, key);
    else {
        if(!root->left) { Node* r=root->right; delete root; return r; }
        if(!root->right){ Node* l=root->left;  delete root; return l; }
        int succ = minValue(root->right);
        root->key = succ;
        root->right = deleteNode(root->right, succ);
    }
    return root;
}
""";
            case TRAVERSALS -> """
// C++ — Tree Traversals
void preorder(Node* root){
    if(!root) return;
    cout<<root->key<<" ";
    preorder(root->left);
    preorder(root->right);
}
void inorder(Node* root){
    if(!root) return;
    inorder(root->left);
    cout<<root->key<<" ";
    inorder(root->right);
}
void postorder(Node* root){
    if(!root) return;
    postorder(root->left);
    postorder(root->right);
    cout<<root->key<<" ";
}
""";
            case AVL_INSERT -> """
// C++ — AVL Insert (with rotations)
int height(Node* n){ return n? n->height: 0; }
int balance(Node* n){ return n? height(n->left)-height(n->right): 0; }

Node* rightRotate(Node* y){
    Node* x = y->left;
    Node* T2 = x->right;
    x->right = y;
    y->left = T2;
    y->height = 1 + max(height(y->left), height(y->right));
    x->height = 1 + max(height(x->left), height(x->right));
    return x;
}
Node* leftRotate(Node* x){
    Node* y = x->right;
    Node* T2 = y->left;
    y->left = x;
    x->right = T2;
    x->height = 1 + max(height(x->left), height(x->right));
    y->height = 1 + max(height(y->left), height(y->right));
    return y;
}
Node* avlInsert(Node* node, int key){
    if(!node) { Node* n = new Node(key); n->height=1; return n; }
    if(key < node->key) node->left = avlInsert(node->left, key);
    else if(key > node->key) node->right = avlInsert(node->right, key);
    else return node;

    node->height = 1 + max(height(node->left), height(node->right));
    int b = balance(node);

    if(b>1 && key<node->left->key)  return rightRotate(node);          // LL
    if(b<-1 && key>node->right->key) return leftRotate(node);          // RR
    if(b>1 && key>node->left->key){ node->left=leftRotate(node->left); return rightRotate(node);} // LR
    if(b<-1 && key<node->right->key){ node->right=rightRotate(node->right); return leftRotate(node);} // RL
    return node;
}
""";
            default -> "// C++ pseudocode unavailable for this algorithm.";
        };
    }

    public static String python(AlgorithmId id){
        return switch (id) {
            case BUBBLE_SORT -> """
# Python — Bubble Sort
def bubble_sort(a):
    n = len(a)
    swapped = True
    while swapped:
        swapped = False
        for i in range(1, n):
            if a[i-1] > a[i]:
                a[i-1], a[i] = a[i], a[i-1]
                swapped = True
        n -= 1
""";
            case SELECTION_SORT -> """
# Python — Selection Sort
def selection_sort(a):
    n = len(a)
    for i in range(n):
        min_idx = i
        for j in range(i + 1, n):
            if a[j] < a[min_idx]:
                min_idx = j
        a[i], a[min_idx] = a[min_idx], a[i]
""";
            case INSERTION_SORT -> """
# Python — Insertion Sort
def insertion_sort(a):
    for i in range(1, len(a)):
        key = a[i]
        j = i - 1
        while j >= 0 and key < a[j] :
                a[j + 1] = a[j]
                j -= 1
        a[j + 1] = key
""";
            case BFS -> """
# Python — BFS
from collections import deque
def bfs(adj, s):
    dist = [-1]*len(adj)
    q = deque([s])
    dist[s]=0
    while q:
        u = q.popleft()
        for v in adj[u]:
            if dist[v] == -1:
                dist[v] = dist[u] + 1
                q.append(v)
    return dist
""";
            case DFS -> """
# Python — DFS (recursive)
def dfs(adj, s):
    n = len(adj)
    vis = [False]*n
    def rec(u):
        vis[u] = True
        for v in adj[u]:
            if not vis[v]:
                rec(v)
    rec(s)
    return vis
""";
            case DIJKSTRA -> """
# Python — Dijkstra's Algorithm
import heapq
def dijkstra(adj, s):
    dist = {node: float('inf') for node in adj}
    dist[s] = 0
    pq = [(0, s)]
    while pq:
        d, u = heapq.heappop(pq)
        if d > dist[u]:
            continue
        for v, weight in adj[u].items():
            if dist[u] + weight < dist[v]:
                dist[v] = dist[u] + weight
                heapq.heappush(pq, (dist[v], v))
    return dist
""";
            case ASTAR -> """
# Python — A* Search
import heapq
def a_star(adj, start, goal, heuristic):
    g = {u: float('inf') for u in adj}
    f = {u: float('inf') for u in adj}
    parent = {u: None for u in adj}
    g[start] = 0.0
    f[start] = heuristic(start)
    pq = [(f[start], start)]
    visited = set()

    while pq:
        _, u = heapq.heappop(pq)
        if u in visited: 
            continue
        visited.add(u)
        if u == goal:
            break
        for v, w in adj[u].items():
            tentative = g[u] + w
            if tentative < g[v]:
                g[v] = tentative
                f[v] = tentative + heuristic(v)
                parent[v] = u
                heapq.heappush(pq, (f[v], v))

    # reconstruct path
    path = []
    if parent[goal] is None and start != goal:
        return path
    cur = goal
    while cur is not None:
        path.append(cur)
        cur = parent[cur]
    path.reverse()
    return path
""";
            case KRUSKAL -> """
# Python — Kruskal's MST
class DSU:
    def __init__(self, n):
        self.p = list(range(n))
    def find(self, x):
        if self.p[x] != x:
            self.p[x] = self.find(self.p[x])
        return self.p[x]
    def unite(self, a, b):
        ra, rb = self.find(a), self.find(b)
        if ra == rb: 
            return False
        self.p[ra] = rb
        return True

def kruskal(n, edges):
    # edges: list of (w,u,v)
    edges.sort()
    dsu = DSU(n)
    mst = []
    for w,u,v in edges:
        if dsu.unite(u,v):
            mst.append((u,v,w))
            if len(mst) == n-1:
                break
    return mst
""";
            case PRIM -> """
# Python — Prim's MST (min-heap)
import heapq
def prim(adj, start=0):
    n = len(adj)
    in_mst = [False]*n
    key = [float('inf')]*n
    parent = [-1]*n
    key[start]=0.0
    pq=[(0.0, start)]
    mst=[]
    while pq:
        _, u = heapq.heappop(pq)
        if in_mst[u]: 
            continue
        in_mst[u]=True
        if parent[u] != -1:
            mst.append((parent[u], u, key[u]))
        for v, w in adj[u].items():
            if not in_mst[v] and w < key[v]:
                key[v] = w
                parent[v] = u
                heapq.heappush(pq, (key[v], v))
    return mst
""";
            case MERGE_SORT -> """
# Python — Merge Sort (top-down)
def merge(a, l, m, r):
    L = a[l:m+1]
    R = a[m+1:r+1]
    i=j=0; k=l
    while i<len(L) and j<len(R):
        if L[i] <= R[j]:
            a[k]=L[i]; i+=1
        else:
            a[k]=R[j]; j+=1
        k+=1
    while i<len(L):
        a[k]=L[i]; i+=1; k+=1
    while j<len(R):
        a[k]=R[j]; j+=1; k+=1

def merge_sort(a, l=0, r=None):
    if r is None: r = len(a)-1
    if l >= r: return
    m = (l+r)//2
    merge_sort(a, l, m)
    merge_sort(a, m+1, r)
    merge(a, l, m, r)
""";
            case QUICK_SORT -> """
# Python — Quick Sort (Lomuto partition)
def partition(a, l, r):
    pivot = a[r]
    i = l
    for j in range(l, r):
        if a[j] < pivot:
            a[i], a[j] = a[j], a[i]
            i += 1
    a[i], a[r] = a[r], a[i]
    return i

def quick_sort(a, l=0, r=None):
    if r is None: r = len(a)-1
    if l < r:
        p = partition(a, l, r)
        quick_sort(a, l, p-1)
        quick_sort(a, p+1, r)
""";
            case HEAP_SORT -> """
# Python — Heap Sort (max-heap)
def heapify(a, n, i):
    largest = i
    L, R = 2*i+1, 2*i+2
    if L < n and a[L] > a[largest]: largest = L
    if R < n and a[R] > a[largest]: largest = R
    if largest != i:
        a[i], a[largest] = a[largest], a[i]
        heapify(a, n, largest)

def heap_sort(a):
    n = len(a)
    for i in range(n//2-1, -1, -1):
        heapify(a, n, i)
    for i in range(n-1, 0, -1):
        a[0], a[i] = a[i], a[0]
        heapify(a, i, 0)
""";
            case BST_INSERT -> """
# Python — BST Insert
class Node:
    def __init__(self, key):
        self.key = key
        self.left = None
        self.right = None

def bst_insert(root, key):
    if root is None:
        return Node(key)
    if key < root.key:
        root.left = bst_insert(root.left, key)
    elif key > root.key:
        root.right = bst_insert(root.right, key)
    return root
""";
            case BST_DELETE -> """
# Python — BST Delete
def min_value(node):
    while node.left:
        node = node.left
    return node.key

def bst_delete(root, key):
    if root is None: 
        return root
    if key < root.key:
        root.left = bst_delete(root.left, key)
    elif key > root.key:
        root.right = bst_delete(root.right, key)
    else:
        if root.left is None:
            return root.right
        if root.right is None:
            return root.left
        succ = min_value(root.right)
        root.key = succ
        root.right = bst_delete(root.right, succ)
    return root
""";
            case TRAVERSALS -> """
# Python — Tree Traversals
def preorder(node, out):
    if not node: return
    out.append(node.key)
    preorder(node.left, out)
    preorder(node.right, out)

def inorder(node, out):
    if not node: return
    inorder(node.left, out)
    out.append(node.key)
    inorder(node.right, out)

def postorder(node, out):
    if not node: return
    postorder(node.left, out)
    postorder(node.right, out)
    out.append(node.key)
""";
            case AVL_INSERT -> """
# Python — AVL Insert (with rotations)
class AVLNode:
    def __init__(self, key):
        self.key = key
        self.left = None
        self.right = None
        self.height = 1

def h(n): return n.height if n else 0
def bal(n): return h(n.left)-h(n.right) if n else 0

def rotate_right(y):
    x = y.left; T2 = x.right
    x.right = y; y.left = T2
    y.height = 1 + max(h(y.left), h(y.right))
    x.height = 1 + max(h(x.left), h(x.right))
    return x

def rotate_left(x):
    y = x.right; T2 = y.left
    y.left = x; x.right = T2
    x.height = 1 + max(h(x.left), h(x.right))
    y.height = 1 + max(h(y.left), h(y.right))
    return y

def avl_insert(node, key):
    if node is None:
        return AVLNode(key)
    if key < node.key:
        node.left = avl_insert(node.left, key)
    elif key > node.key:
        node.right = avl_insert(node.right, key)
    else:
        return node

    node.height = 1 + max(h(node.left), h(node.right))
    b = bal(node)

    if b > 1 and key < node.left.key:      # LL
        return rotate_right(node)
    if b < -1 and key > node.right.key:    # RR
        return rotate_left(node)
    if b > 1 and key > node.left.key:      # LR
        node.left = rotate_left(node.left)
        return rotate_right(node)
    if b < -1 and key < node.right.key:    # RL
        node.right = rotate_right(node.right)
        return rotate_left(node)
    return node
""";
            default -> "# Python pseudocode unavailable for this algorithm.";
        };
    }

    private PseudocodeRepository(){}
}
