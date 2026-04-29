// Uncomment this line to use CSS modules
// import styles from './app.module.css';
import React, {useEffect, useState} from 'react';
import ForceGraph2D from 'react-force-graph-2d';

type Person = { id: number; name: string };

export function App() {
  const [data, setData] = useState({nodes: [], links: []});
  const [people, setPeople] = useState<Person[]>([]);
  const [selectedId, setSelectedId] = useState<string>('');
  const [newName, setNewName] = useState<string>('');

  const fetchGraph = () => {
    fetch('/api/person/graph/db')
      .then(res => res.json())
      .then(json => setData(json))
      .catch(err => console.error('Error fetching graph:', err));
  };

  const fetchPeople = () => {
    fetch('/api/person')
      .then(res => res.json())
      .then((json: Person[]) => {
        setPeople(json);
        if (json.length && !selectedId) setSelectedId(String(json[0].id));
      })
      .catch(err => console.error('Error fetching people:', err));
  };

  useEffect(() => {
    fetchGraph();
    fetchPeople();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!selectedId || !newName) return;
    try {
      const res = await fetch(
        `/create-node-and-relation?name=${encodeURIComponent(newName)}&id=${selectedId}`
      );
      const text = await res.text();
      console.log('create-node-and-relation:', text);
      setNewName('');
      fetchGraph();
      fetchPeople();
    } catch (err) {
      console.error('Error creating node and relation:', err);
    }
  };

  return (
    <div style={{width: '100vw', height: '100vh', display: 'flex', flexDirection: 'column'}}>
      <form onSubmit={handleSubmit} style={{padding: 12, display: 'flex', gap: 8, alignItems: 'center'}}>
        <label style={{display: 'flex', gap: 6, alignItems: 'center'}}>
          Select person:
          <select value={selectedId} onChange={e => setSelectedId(e.target.value)}>
            {people.map(p => (
              <option key={p.id} value={String(p.id)}>{p.name}</option>
            ))}
          </select>
        </label>

        <label style={{display: 'flex', gap: 6, alignItems: 'center'}}>
          New name:
          <input value={newName} onChange={e => setNewName(e.target.value)} placeholder="Name for new node" />
        </label>

        <button type="submit">Create node + relation</button>
      </form>

      <div style={{flex: 1}}>
        <ForceGraph2D
          graphData={data}
          nodeLabel={n => n.id}
          nodeAutoColorBy={n => n.group}
          linkDirectionalArrowLength={6}
          linkDirectionalArrowRelPos={1}
          nodeCanvasObject={(node, ctx, globalScale) => {
            const label = node.id;
            const fontSize = 12 / globalScale;
            ctx.fillStyle = (node as any).color || 'orange';
            ctx.beginPath();
            ctx.arc((node as any).x, (node as any).y, 6, 0, 2 * Math.PI, false);
            ctx.fill();
            ctx.font = `${fontSize}px Sans-Serif`;
            ctx.textAlign = 'center';
            ctx.textBaseline = 'top';
            ctx.fillStyle = '#222';
            ctx.fillText(String(label), (node as any).x, (node as any).y + 8);
          }}
        />
      </div>
    </div>
  );
}

export default App;
