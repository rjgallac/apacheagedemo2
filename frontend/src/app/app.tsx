// Uncomment this line to use CSS modules
// import styles from './app.module.css';
import React, {useEffect, useRef, useState} from 'react';
import ForceGraph2D from 'react-force-graph-2d';

export function App() {
  const [data, setData] = useState({nodes: [], links: []});

  useEffect(() => {
    fetch('/graph/db')
      .then(res => res.json())
      .then(json => setData(json))
      .catch(err => console.error('Error fetching graph:', err));
  }, []);

  return (
    <div style={{width: '100vw', height: '100vh'}}>
      <ForceGraph2D
        graphData={data}
        nodeLabel={n => n.id}
        nodeAutoColorBy={n => n.group}
        linkDirectionalArrowLength={6}
        linkDirectionalArrowRelPos={1}
        nodeCanvasObject={(node, ctx, globalScale) => {
          const label = node.id;
          const fontSize = 12 / globalScale;
          ctx.fillStyle = node.color || 'orange';
          ctx.beginPath();
          ctx.arc(node.x, node.y, 6, 0, 2 * Math.PI, false);
          ctx.fill();
          ctx.font = `${fontSize}px Sans-Serif`;
          ctx.textAlign = 'center';
          ctx.textBaseline = 'top';
          ctx.fillStyle = '#222';
          ctx.fillText(label, node.x, node.y + 8);
        }}
      />
    </div>
  );
}

export default App;
