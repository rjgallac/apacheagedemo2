import { useState } from "react";

export default function TeamForm({ refresh } :any) {

    const [teamName, setTeamName] = useState<string>('');

    const getTeams = () => {
        fetch('/api/team')
        .then(res => res.json())
        .then(json => console.log('teams:', json))
        .catch(err => console.error('Error fetching teams:', err));
    }

    const addTeam = async (e: React.FormEvent) => {
        e.preventDefault();
        const formData = new FormData(e.target as HTMLFormElement);
        const teamName = formData.get('teamName') as string;
        if (!teamName) return;
        try {
            const res = await fetch('/api/team', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ name: teamName })
            });
            if (!res.ok) {
                throw new Error('Failed to add team');
            }
        } catch (error) {
            console.error('Error adding team:', error);
        }
    };

    const deleteTeam = async (e: React.FormEvent) => {
        e.preventDefault();
        const formData = new FormData(e.target as HTMLFormElement);
        const teamId = formData.get('teamId') as string;
        if (!teamId) return;
        try {
            const res = await fetch(`/api/team/${teamId}`, {
                method: 'DELETE'
            });
            if (!res.ok) {
                throw new Error('Failed to delete team');
            }
        } catch (error) {
            console.error('Error deleting team:', error);
        }
    };



    return (
        <div>
            <h4>Add Team</h4>

            <form onSubmit={addTeam}>
                <label>Team Name:
                    <input type="text" name="teamName" />
                </label>
                <button type="submit" style={{width: 'stretch', backgroundColor: 'seagreen', borderRadius: '5px', padding: '10px', marginTop: '10px'}}>Add Team</button>
            </form>
            <h4>link team to dept</h4>
            <h4>link team to person</h4>

            <h4>Delete Team</h4>
            <form onSubmit={deleteTeam}>
                <label>select team:</label>
                <select name="teamId">
                    <option value="">-- Select Team --</option>
                </select>
                <button type="submit" style={{width: 'stretch', backgroundColor: 'red', borderRadius: '5px', padding: '10px', marginTop: '10px'}}>Delete Team</button>
            </form>
        </div>
    );
}