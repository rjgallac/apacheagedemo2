// Uncomment this line to use CSS modules
// import styles from './app.module.css';
import React, {useEffect, useState} from 'react';
import ForceGraph2D from 'react-force-graph-2d';
import PersonForm from './PersonForm';
import CompanyForm from './CompanyForm';
import DeptForm from './DeptForm';
import ProjectForm from './ProjectForm';
import TeamForm from './TeamForm';

export function App() {
  const [data, setData] = useState({nodes: [], links: []});


  const fetchGraph = () => {
    fetch('/api/person/graph/db')
      .then(res => res.json())
      .then(json => setData(json))
      .catch(err => console.error('Error fetching graph:', err));
  };

  useEffect(() => {
    fetchGraph();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  return (
    <div>
      <div className="header">
        <h1>Your Company Tree</h1>
      </div>
      <div style={{width: '100vw', height: '100vh', display: 'flex', flexDirection: 'row'}}>
        <div style={{flex: '0 0 20%', display: 'flex', flexDirection: 'column', backgroundColor: 'steelblue', opacity: '80%', padding: 12, gap: 12, overflow: 'scroll'}}>
          <details style={{cursor: 'pointer'}}>
            <summary className="menu" >Company</summary>
            <CompanyForm refresh={fetchGraph}/>
          </details>
          <details>
            <summary className="menu" >Dept</summary>
            <DeptForm refresh={fetchGraph}/>
          </details>
          <details>
            <summary className="menu" >Team</summary>
            <TeamForm refresh={fetchGraph} />
          </details>
          <details>
            <summary className="menu" >Projects</summary> 
            <ProjectForm />
          </details>
          <details>
            <summary className="menu" >Person</summary>
            <PersonForm refresh={fetchGraph} />
          </details>
        </div>
        <div style={{flex: 1, backgroundColor: 'aliceblue'}}>
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
    </div>
  );
}

export default App;
