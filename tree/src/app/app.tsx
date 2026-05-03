// Simple tree menu that fetches data from backend and renders
import React, { useEffect, useState } from 'react';

type TreeData = Array<Record<string, any[]>>;

function TreeNode({ node }: { node: Record<string, any[]> }) {
  const entries = Object.entries(node);
  return (
    <>
      {entries.map(([name, children]) => (
        <details key={name} style={{ marginLeft: 8, marginBottom: 4 }}>
          <summary>{name}</summary>
          {Array.isArray(children) && children.length > 0 ? (
            <div style={{ marginLeft: 12 }}>
              {children.map((child, idx) => (
                <TreeNode key={idx} node={child} />
              ))}
            </div>
          ) : null}
        </details>
      ))}
    </>
  );
}

export function App() {
  const [data, setData] = useState<TreeData | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    let cancelled = false;
    async function fetchTree() {
      try {
        const res = await fetch('/api/tree');
        if (!res.ok) throw new Error(`HTTP ${res.status}`);
        const json = await res.json();
        if (!cancelled) setData(json);
      } catch (e) {
        if (!cancelled) setError((e as Error).message);
      } finally {
        if (!cancelled) setLoading(false);
      }
    }
    fetchTree();
    return () => {
      cancelled = true;
    };
  }, []);

  if (loading) return <div>Loading tree...</div>;
  if (error) return <div>Error: {error}</div>;
  if (!data || data.length === 0) return <div>No tree data</div>;

  return (
    <div>
      <h2>Tree Menu</h2>
      <div>
        {data.map((node, idx) => (
          <TreeNode key={idx} node={node} />
        ))}
      </div>
    </div>
  );
}

export default App;
