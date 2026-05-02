// Uncomment this line to use CSS modules
// import styles from './app.module.css';
import React, {useEffect, useState, useRef} from 'react';
import ForceGraph2D from 'react-force-graph-2d';
import MainForm from './MainForm';

export function App() {
  const [data, setData] = useState({nodes: [], links: []});
  const [nodeFilter, setNodeFilter] = useState<string[]>(["Company", "Person", "Project", "Team", "Department"]);
  const [relationFilter, setRelationFilter] = useState<string[]>(["EMPLOYED_AT", "WORKS_ON", "MANAGES", "MEMBER_OF", "PART_OF"]);
  const fgRef = useRef<any>(null);
  const fetchGraph = () => {
    fetch(`/api/graph?nodeFilter=${nodeFilter.join(',')}&relationFilter=${relationFilter.join(',')}`)
      .then(res => res.json())
      .then(json => setData(json))
      .catch(err => console.error('Error fetching graph:', err));
  };
  const handleNodeFilterChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, checked } = e.target;
    setNodeFilter(prev => {
      if (checked) {
        return prev.includes(name) ? prev : [...prev, name];
      } else {
        return prev.filter(n => n !== name);
      }
    });
  }
  const handleRelationFilterChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, checked, value } = e.target;
    const key = value || name;
    setRelationFilter(prev => {
      if (checked) {
        return prev.includes(key) ? prev : [...prev, key];
      } else {
        return prev.filter(r => r !== key);
      }
    });
  }

  useEffect(() => {
    fetchGraph();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [nodeFilter, relationFilter]);

  // Zoom to fit when the data updates
  useEffect(() => {
    if (!fgRef.current) return;
    const t = setTimeout(() => {
      try {
        fgRef.current.zoomToFit(400, 20);
      } catch (err) {
        // ignore if method is unavailable
      }
    }, 120);
    return () => clearTimeout(t);
  }, [data]);

  const isNodeChecked = (type: string) => nodeFilter.includes(type);
  const isRelationChecked = (type: string) => relationFilter.includes(type);

  return (
    <div>
      <div className="header">
        <h1>Your Company Tree</h1>
      </div>
      <div style={{width: '100vw', height: '100vh', display: 'flex', flexDirection: 'row'}}>
        <div style={{flex: '0 0 20%', display: 'flex', flexDirection: 'column', backgroundColor: 'steelblue', opacity: '80%', padding: 12, gap: 12, overflow: 'scroll'}}>

          <MainForm refresh={fetchGraph}/>
          <label>
              Filter Nodes
              <input type="checkbox" name="Company" checked={isNodeChecked("Company")} onChange={handleNodeFilterChange} />Company
              <input type="checkbox" name="Person" checked={isNodeChecked("Person")} onChange={handleNodeFilterChange} />Person
              <input type="checkbox" name="Project" checked={isNodeChecked("Project")} onChange={handleNodeFilterChange}  />Project
          </label>
         
         <label>
              Filter Relationships
              <input type="checkbox" name="EMPLOYED_AT" checked={isRelationChecked("EMPLOYED_AT")} onChange={handleRelationFilterChange} value="EMPLOYED_AT" />EMPLOYED_AT
              <input type="checkbox" name="WORKS_ON" checked={isRelationChecked("WORKS_ON")} onChange={handleRelationFilterChange} value="WORKS_ON" />WORKS_ON
              <input type="checkbox" name="MANAGES" checked={isRelationChecked("MANAGES")} onChange={handleRelationFilterChange} value="MANAGES" />MANAGES
              <input type="checkbox" name="MEMBER_OF" checked={isRelationChecked("MEMBER_OF")} onChange={handleRelationFilterChange} value="MEMBER_OF" />MEMBER_OF
              <input type="checkbox" name="PART_OF" checked={isRelationChecked("PART_OF")} onChange={handleRelationFilterChange} value="PART_OF" />PART_OF
         </label>
          
        </div>
        <div style={{flex: 1, backgroundColor: 'aliceblue'}}>
          <ForceGraph2D
            ref={fgRef}
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
